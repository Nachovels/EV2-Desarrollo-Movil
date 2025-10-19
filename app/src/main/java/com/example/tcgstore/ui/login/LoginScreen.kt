package com.example.tcgstore.ui.login

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.TextButton
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

fun LoginScreen(navController: NavController){
    var correo by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }

    var isCorreoError by remember { mutableStateOf(false) }
    var isContraseñaError by remember { mutableStateOf(false) }

    fun validateCorreo(text: String) {
        isCorreoError = !Patterns.EMAIL_ADDRESS.matcher(text).matches()
    }

    fun validateContraseña(text: String) {
        isContraseñaError = text.length < 6
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Iniciar Sesión") },
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
        ){
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    validateCorreo(it)

                },
                label = { Text("Correo") },
                isError = isCorreoError,
                supportingText = { if (isCorreoError)  Text("Correo inválido") else null }
            )
            OutlinedTextField(
                value = contraseña,
                onValueChange = { contraseña = it
                    validateContraseña(it)
                },
                label = { Text("Contraseña") },
                isError = isContraseñaError,
                supportingText = { if (isContraseñaError)  Text("Contraseña debe tener al menos 6 caracteres") else null }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("home") }) {
                Text("Iniciar Sesión")
            }
            TextButton(onClick = { navController.navigate("register") }) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(){
    val navController = rememberNavController()
    LoginScreen(navController)
}
