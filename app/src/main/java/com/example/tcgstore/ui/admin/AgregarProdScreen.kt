package com.example.tcgstore.ui.admin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tcgstore.data.repository.ApiResult
import com.example.tcgstore.data.repository.ProductRepository
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProductoScreen(navController: NavController) {
    val context = LocalContext.current
    val productRepository = remember { ProductRepository(context) }
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var oferta by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var hoverUri by remember { mutableStateOf<Uri?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showImageSelector by remember { mutableStateOf(false) }
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { resultUri: Uri? ->
        if (resultUri != null) {
            if (showImageSelector) {
                imageUri = resultUri
            } else {
                hoverUri = resultUri
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            if (showImageSelector) {
                imageUri = tempUri
            } else {
                hoverUri = tempUri
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permiso Aceptado", Toast.LENGTH_SHORT).show()
            // Crear archivo y URI antes de lanzar la cámara
            val file = context.createImageFile()
            val uri = FileProvider.getUriForFile(
                Objects.requireNonNull(context),
                context.packageName + ".provider",
                file
            )
            tempUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permiso Denegado. No se puede acceder a la cámara.", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Imagen Principal
            Text("Imagen Principal *", style = MaterialTheme.typography.titleMedium)

            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Imagen del producto",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        showImageSelector = true
                        galleryLauncher.launch("image/*")
                    },
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    Text("Galería")
                }

                Button(
                    onClick = {
                        showImageSelector = true
                        val permissionCheck = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        )
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            // Crear archivo y URI antes de lanzar la cámara si ya tiene permiso
                            val file = context.createImageFile()
                            val uri = FileProvider.getUriForFile(
                                Objects.requireNonNull(context),
                                context.packageName + ".provider",
                                file
                            )
                            tempUri = uri
                            cameraLauncher.launch(uri)
                        } else {
                            // Pedir permiso
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    Text("Cámara")
                }
            }

            Divider()

            // Imagen Hover (opcional)
            Text("Imagen Hover (Opcional)", style = MaterialTheme.typography.titleMedium)

            if (hoverUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(hoverUri),
                    contentDescription = "Imagen hover",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        showImageSelector = false
                        galleryLauncher.launch("image/*")
                    },
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    Text("Galería Hover")
                }

                if (hoverUri != null) {
                    OutlinedButton(
                        onClick = { hoverUri = null },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Quitar")
                    }
                }
            }

            Divider()

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    errorMessage = null
                },
                label = { Text("Nombre del Producto *") },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                    errorMessage = null
                },
                label = { Text("Descripción *") },
                enabled = !isLoading,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = {
                    precio = it.filter { char -> char.isDigit() }
                    errorMessage = null
                },
                label = { Text("Precio *") },
                enabled = !isLoading,
                prefix = { Text("$") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = oferta,
                onValueChange = {
                    oferta = it
                    errorMessage = null
                },
                label = { Text("Etiqueta de Oferta (Opcional)") },
                enabled = !isLoading,
                placeholder = { Text("Ej: NUEVO, OFERTA, etc.") },
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (imageUri == null) {
                        errorMessage = "Debe seleccionar una imagen principal"
                        return@Button
                    }

                    if (nombre.isBlank() || descripcion.isBlank() || precio.isBlank()) {
                        errorMessage = "Complete todos los campos obligatorios"
                        return@Button
                    }

                    scope.launch {
                        isLoading = true
                        errorMessage = null

                        when (val result = productRepository.createProduct(
                            nombre = nombre,
                            descripcion = descripcion,
                            precio = precio.toIntOrNull() ?: 0,
                            imagenUri = imageUri!!,
                            oferta = oferta.takeIf { it.isNotBlank() },
                            hoverUri = hoverUri
                        )) {
                            is ApiResult.Success -> {
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "Producto creado exitosamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.popBackStack()
                            }
                            is ApiResult.Error -> {
                                isLoading = false
                                errorMessage = result.message
                            }
                            else -> {}
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar Producto")
                }
            }
        }
    }
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}
