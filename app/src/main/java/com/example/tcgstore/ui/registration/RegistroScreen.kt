package com.example.tcgstore.ui.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tcgstore.data.UserStorage
import com.example.tcgstore.data.Usuario
import com.example.tcgstore.utils.ValidationUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavController) {
    val context = LocalContext.current
    val userStorage = UserStorage(context)
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var contrasenaVisible by remember { mutableStateOf(false) }

    var isNombreError by remember { mutableStateOf(false) }
    var isApellidoError by remember { mutableStateOf(false) }
    var isRutError by remember { mutableStateOf(false) }
    var isCorreoError by remember { mutableStateOf(false) }
    var isTelefonoError by remember { mutableStateOf(false) }
    var isContrasenaError by remember { mutableStateOf(false) }
    var isConfirmarContrasenaError by remember { mutableStateOf(false) }

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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    isNombreError = !ValidationUtils.isValidNombre(it)
                },
                label = { Text("Nombre") },
                isError = isNombreError,
                supportingText = { if (isNombreError) Text("El nombre no puede contener números") else null }
            )
            OutlinedTextField(
                value = apellido,
                onValueChange = {
                    apellido = it
                    isApellidoError = !ValidationUtils.isValidApellido(it)
                },
                label = { Text("Apellido") },
                isError = isApellidoError,
                supportingText = { if (isApellidoError) Text("El apellido no puede contener números") else null }
            )
            OutlinedTextField(
                value = rut,
                onValueChange = {
                    rut = it
                    isRutError = !ValidationUtils.isValidRut(it)
                },
                label = { Text("RUT") },
                isError = isRutError,
                supportingText = { if (isRutError) Text("Formato de RUT no válido") else null }
            )
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    isCorreoError = !ValidationUtils.isValidCorreo(it)
                },
                label = { Text("Correo electrónico") },
                isError = isCorreoError,
                supportingText = { if (isCorreoError) Text("Formato de correo no válido") else null }
            )
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") }
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { 
                    telefono = it
                    isTelefonoError = !ValidationUtils.isValidTelefono(it)
                },
                label = { Text("Teléfono") },
                isError = isTelefonoError,
                supportingText = { if (isTelefonoError) Text("Formato de teléfono no válido") else null }
            )

            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    isContrasenaError = !ValidationUtils.isValidContrasena(it)
                    isConfirmarContrasenaError = !ValidationUtils.contrasenasIguales(it, confirmarContrasena)
                },
                label = { Text("Contraseña") },
                isError = isContrasenaError,
                supportingText = { if (isContrasenaError) Text("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial") else null },
                visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (contrasenaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (contrasenaVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            )

            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = {
                    confirmarContrasena = it
                    isConfirmarContrasenaError = !ValidationUtils.contrasenasIguales(contrasena, it)
                },
                label = { Text("Confirmar Contraseña") },
                isError = isConfirmarContrasenaError,
                supportingText = { if (isConfirmarContrasenaError) Text("Las contraseñas no coinciden") else null },
                visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (contrasenaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (contrasenaVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            )

            Button(
                onClick = {
                    scope.launch {
                        val usuario = Usuario(nombre, apellido, rut, correo, direccion, telefono, contrasena)
                        userStorage.guardarUsuario(usuario)
                        navController.navigate("registro_exitoso")
                    }
                 },
                shape = RoundedCornerShape(12.dp),
                enabled = !isNombreError && !isApellidoError && !isRutError && !isCorreoError && !isTelefonoError && !isContrasenaError && !isConfirmarContrasenaError &&
                        nombre.isNotEmpty() && apellido.isNotEmpty() && rut.isNotEmpty() && correo.isNotEmpty() && direccion.isNotEmpty() && telefono.isNotEmpty() && contrasena.isNotEmpty() && confirmarContrasena.isNotEmpty()
            ) {
                Text(text = "Registrarse")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    com.example.tcgstore.ui.theme.TCGStoreTheme {
        RegistroScreen(rememberNavController())
    }
}
