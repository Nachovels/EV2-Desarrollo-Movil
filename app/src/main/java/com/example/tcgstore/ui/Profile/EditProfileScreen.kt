package com.example.tcgstore.ui.profile

import android.net.Uri
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.tcgstore.R
import com.example.tcgstore.data.UserStorage
import com.example.tcgstore.ui.theme.TCGStoreTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val userStorage = UserStorage(context)
    val scope = rememberCoroutineScope()

    val loggedInUserEmail by userStorage.loggedInUserEmailFlow.collectAsState(initial = null)
    val users by userStorage.usersFlow.collectAsState(initial = emptyList())
    val currentUser = loggedInUserEmail?.let { email -> users.find { it.correo == email } }

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

    fun validarNombre(text: String) {
        isNombreError = text.any { it.isDigit() }
    }

    fun validarApellido(text: String) {
        isApellidoError = text.any { it.isDigit() }
    }

    fun validarRut(text: String) {
        isRutError = !text.matches(Regex("^[0-9]{7,8}-[0-9kK]$|^[0-9]{1,2}\\.               [0-9]{3}\\.               [0-9]{3}-[0-9kK]$"))
    }

    fun validarTelefono(text: String) {
        isTelefonoError = !text.matches(Regex("^[0-9]{9}$"))
    }

    LaunchedEffect(currentUser) {
        currentUser?.let {
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

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> imageUri = uri }
    )

    val isFormValid = !isNombreError && !isApellidoError && !isRutError && !isTelefonoError &&
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
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
            if (currentUser != null) {
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
                                .clickable { galleryLauncher.launch("image/*") }
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

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it; validarNombre(it) },
                        label = { Text("Nombre") },
                        isError = isNombreError,
                        supportingText = { if (isNombreError) Text("El nombre no puede contener números") else null },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { apellido = it; validarApellido(it) },
                        label = { Text("Apellido") },
                        isError = isApellidoError,
                        supportingText = { if (isApellidoError) Text("El apellido no puede contener números") else null },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = rut,
                        onValueChange = { rut = it; validarRut(it) },
                        label = { Text("RUT") },
                        isError = isRutError,
                        supportingText = { if (isRutError) Text("Formato de RUT no válido") else null },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it; validarTelefono(it) },
                        label = { Text("Teléfono") },
                        isError = isTelefonoError,
                        supportingText = { if (isTelefonoError) Text("Formato de teléfono no válido") else null },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                val updatedUser = currentUser.copy(
                                    nombre = nombre,
                                    apellido = apellido,
                                    rut = rut,
                                    direccion = direccion,
                                    telefono = telefono,
                                    photoUri = imageUri?.toString()
                                )
                                userStorage.updateUser(updatedUser)
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isFormValid
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

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    TCGStoreTheme {
        EditProfileScreen(navController = rememberNavController())
    }
}
