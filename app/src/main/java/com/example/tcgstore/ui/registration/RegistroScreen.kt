package com.example.tcgstore.ui.registration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tcgstore.data.repository.ApiResult
import com.example.tcgstore.data.repository.AuthRepository
import com.example.tcgstore.utils.ValidationUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavController) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    val scope = rememberCoroutineScope()

    var nombreCompleto by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var contrasenaVisible by remember { mutableStateOf(false) }

    var isNombreError by remember { mutableStateOf(false) }
    var isCorreoError by remember { mutableStateOf(false) }
    var isContrasenaError by remember { mutableStateOf(false) }
    var isConfirmarContrasenaError by remember { mutableStateOf(false) }

    var registroError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro") },
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
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = {
                    nombreCompleto = it
                    isNombreError = it.isNotBlank() && it.length < 3
                    registroError = null
                },
                label = { Text("Nombre Completo") },
                enabled = !isLoading,
                isError = isNombreError,
                supportingText = {
                    if (isNombreError) Text("El nombre debe tener al menos 3 caracteres")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    isCorreoError = it.isNotBlank() && !ValidationUtils.isValidCorreo(it)
                    registroError = null
                },
                label = { Text("Correo electrónico") },
                enabled = !isLoading,
                isError = isCorreoError,
                supportingText = {
                    if (isCorreoError) Text("Formato de correo no válido")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    isContrasenaError = it.isNotBlank() && !ValidationUtils.isValidContrasena(it)
                    isConfirmarContrasenaError = confirmarContrasena.isNotBlank() &&
                            !ValidationUtils.contrasenasIguales(it, confirmarContrasena)
                    registroError = null
                },
                label = { Text("Contraseña") },
                enabled = !isLoading,
                isError = isContrasenaError,
                supportingText = {
                    if (isContrasenaError) Text("Mínimo 8 caracteres, una mayúscula, una minúscula y un signo")
                },
                visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (contrasenaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (contrasenaVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = {
                    confirmarContrasena = it
                    isConfirmarContrasenaError = it.isNotBlank() &&
                            !ValidationUtils.contrasenasIguales(contrasena, it)
                    registroError = null
                },
                label = { Text("Confirmar Contraseña") },
                enabled = !isLoading,
                isError = isConfirmarContrasenaError,
                supportingText = {
                    if (isConfirmarContrasenaError) Text("Las contraseñas no coinciden")
                },
                visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            registroError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        registroError = null

                        when (val result = authRepository.register(
                            nombreCompleto = nombreCompleto,
                            correo = correo,
                            password = contrasena,
                            confirmarPassword = confirmarContrasena
                        )) {
                            is ApiResult.Success -> {
                                isLoading = false
                                navController.navigate("registro_exitoso") {
                                    popUpTo("home") { inclusive = false }
                                }
                            }
                            is ApiResult.Error -> {
                                isLoading = false
                                registroError = result.message
                            }
                            else -> {}
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading &&
                        !isNombreError && !isCorreoError && !isContrasenaError && !isConfirmarContrasenaError &&
                        nombreCompleto.isNotEmpty() && correo.isNotEmpty() &&
                        contrasena.isNotEmpty() && confirmarContrasena.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrarse")
                }
            }
        }
    }
}