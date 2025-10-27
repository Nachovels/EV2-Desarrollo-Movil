package com.example.tcgstore.ui.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.tcgstore.R
import com.example.tcgstore.data.UserStorage
import com.example.tcgstore.ui.theme.TCGStoreTheme
import com.example.tcgstore.utils.ValidationUtils
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(navController: NavController) {
    val context = LocalContext.current
    val userStorage = UserStorage(context)
    val scope = rememberCoroutineScope()

    val emailUsuarioLogueado by userStorage.emailUsuarioLogueadoFlow.collectAsState(initial = null)
    val usuarios by userStorage.usuariosFlow.collectAsState(initial = emptyList())
    val usuarioActual = emailUsuarioLogueado?.let { email -> usuarios.find { it.correo == email } }

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var isNombreError by remember { mutableStateOf(false) }
    var isApellidoError by remember { mutableStateOf(false) }
    var isRutError by remember { mutableStateOf(false) }
    var isTelefonoError by remember { mutableStateOf(false) }

    val file = context.crearArchivoDeImagen()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> imageUri = uri }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if(success){
                imageUri = uri
            }
        })


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permiso Aceptado", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permiso Denegado", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(usuarioActual) {
        usuarioActual?.let {
            nombre = it.nombre
            apellido = it.apellido
            rut = it.rut
            direccion = it.direccion
            telefono = it.telefono
            it.photoUri?.let { uriString ->
                imageUri = Uri.parse(uriString)
            }
        }
    }

    val esFormularioValido = !isNombreError && !isApellidoError && !isRutError && !isTelefonoError &&
            nombre.isNotEmpty() && apellido.isNotEmpty() && rut.isNotEmpty() && direccion.isNotEmpty() && telefono.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues), contentAlignment = Alignment.Center) {
            if (usuarioActual != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        val painter = rememberAsyncImagePainter(model = imageUri ?: R.drawable.tcgstore)

                        Image(
                            painter = painter,
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar foto",
                            modifier = Modifier
                                .size(30.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .padding(4.dp),
                            tint = Color.White
                        )
                    }
                    Row {
                        Button(onClick = { galleryLauncher.launch("image/*") },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Galería")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = {
                            val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(uri)
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }, shape = RoundedCornerShape(12.dp)) {
                            Text("Cámara")
                        }
                    }


                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { text ->
                            nombre = text
                            isNombreError = !ValidationUtils.isValidNombre(text)
                        },
                        label = { Text("Nombre") },
                        isError = isNombreError,
                        supportingText = { if (isNombreError) Text("El nombre no puede contener números") else null },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { text ->
                            apellido = text
                            isApellidoError = !ValidationUtils.isValidApellido(text)
                        },
                        label = { Text("Apellido") },
                        isError = isApellidoError,
                        supportingText = { if (isApellidoError) Text("El apellido no puede contener números") else null },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = rut,
                        onValueChange = { text ->
                            rut = text
                            isRutError = !ValidationUtils.isValidRut(text)
                        },
                        label = { Text("RUT") },
                        isError = isRutError,
                        supportingText = { if (isRutError) Text("Formato de RUT no válido") else null },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { text -> direccion = text },
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { text ->
                            telefono = text
                            isTelefonoError = !ValidationUtils.isValidTelefono(text)
                        },
                        label = { Text("Teléfono") },
                        isError = isTelefonoError,
                        supportingText = { if (isTelefonoError) Text("Formato de teléfono no válido") else null },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                val usuarioActualizado = usuarioActual.copy(
                                    nombre = nombre,
                                    apellido = apellido,
                                    rut = rut,
                                    direccion = direccion,
                                    telefono = telefono,
                                    photoUri = imageUri?.toString()
                                )
                                userStorage.actualizarUsuario(usuarioActualizado)
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = esFormularioValido
                    ) {
                        Text("Guardar Cambios")
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}

private fun Context.crearArchivoDeImagen(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    TCGStoreTheme {
        EditarPerfilScreen(navController = rememberNavController())
    }
}
