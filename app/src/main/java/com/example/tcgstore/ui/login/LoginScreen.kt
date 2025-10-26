package com.example.tcgstore.ui.login

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tcgstore.R
import com.example.tcgstore.data.LoginAttempt
import com.example.tcgstore.data.UserStorage
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController){
    val context = LocalContext.current
    val userStorage = UserStorage(context)
    val scope = rememberCoroutineScope()
    val usuarios by userStorage.usersFlow.collectAsState(initial = emptyList())

    var correo by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }

    var contrasenaVisible by remember { mutableStateOf(false) }

    var isCorreoError by remember { mutableStateOf(false) }
    var isContraseñaError by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }

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
            Image(painter = painterResource(id = R.drawable.tcgstore),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 32.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    validateCorreo(it)
                    loginError = null
                },
                label = { Text("Correo") },
                isError = isCorreoError,
                supportingText = { if (isCorreoError)  Text("Correo inválido") else null }
            )
            OutlinedTextField(
                value = contraseña,
                onValueChange = {
                    contraseña = it
                    validateContraseña(it)
                    loginError = null
                },
                label = { Text("Contraseña") },
                isError = isContraseñaError,
                supportingText = { if (isContraseñaError)  Text("Contraseña debe tener al menos 6 caracteres") else null },
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
            loginError?.let {
                Text(text = it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { 
                scope.launch {
                    val isAdmin = correo == "admin@tcg.cl" && contraseña == "admin"
                    val user = usuarios.find { it.correo == correo && it.contrasena == contraseña }
                    val isUser = user != null

                    val attempt = LoginAttempt(correo, success = isAdmin || isUser)
                    userStorage.addLoginAttempt(attempt)

                    if (isAdmin) {
                        navController.navigate("admin")
                    } else if (isUser) {
                        user?.let { userStorage.saveLoggedInUserEmail(it.correo) }
                        navController.navigate("welcome")
                    } else {
                        loginError = "Correo o contraseña incorrectos"
                    }
                }

             },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
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
