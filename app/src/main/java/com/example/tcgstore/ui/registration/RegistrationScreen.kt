package com.example.tcgstore.ui.registration

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    var isNombreError by remember { mutableStateOf(false) }
    var isApellidoError by remember { mutableStateOf(false) }
    var isRutError by remember { mutableStateOf(false) }
    var isCorreoError by remember { mutableStateOf(false) }

    fun validateNombre(text: String) {
        isNombreError = text.any { it.isDigit() }
    }

    fun validateApellido(text: String) {
        isApellidoError = text.any { it.isDigit() }
    }

    fun validateRut(text: String) {
        isRutError = !text.matches(Regex("^[0-9]{7,8}-[0-9kK]$|^[0-9]{1,2}\\.[0-9]{3}\\.[0-9]{3}-[0-9kK]$"))
    }

    fun validateCorreo(text: String) {
        isCorreoError = !Patterns.EMAIL_ADDRESS.matcher(text).matches()
    }

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
                    validateNombre(it)
                },
                label = { Text("Nombre") },
                isError = isNombreError,
                supportingText = { if (isNombreError) Text("El nombre no puede contener números") else null }
            )
            OutlinedTextField(
                value = apellido,
                onValueChange = {
                    apellido = it
                    validateApellido(it)
                },
                label = { Text("Apellido") },
                isError = isApellidoError,
                supportingText = { if (isApellidoError) Text("El apellido no puede contener números") else null }
            )
            OutlinedTextField(
                value = rut,
                onValueChange = {
                    rut = it
                    validateRut(it)
                },
                label = { Text("RUT") },
                isError = isRutError,
                supportingText = { if (isRutError) Text("Formato de RUT no válido") else null }
            )
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    validateCorreo(it)
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
            Button(
                onClick = { /*Aqui se aprieta para poder registrar*/ },
                enabled = !isNombreError && !isApellidoError && !isRutError && !isCorreoError &&
                        nombre.isNotEmpty() && apellido.isNotEmpty() && rut.isNotEmpty() && correo.isNotEmpty() && direccion.isNotEmpty()
            ) {
                Text(text = "Registrarse")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    _root_ide_package_.com.example.tcgstore.ui.theme.TCGStoreTheme {
        RegistrationScreen(rememberNavController())
    }
}