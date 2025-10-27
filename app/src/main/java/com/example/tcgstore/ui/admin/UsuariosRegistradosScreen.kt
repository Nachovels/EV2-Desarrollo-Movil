package com.example.tcgstore.ui.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tcgstore.data.UserStorage
import com.example.tcgstore.ui.registration.RegistroExitosoScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosRegistradosScreen(navController: NavController) {
    val context = LocalContext.current
    val userStorage = UserStorage(context)
    val usuarios by userStorage.usuariosFlow.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuarios registrados") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items(usuarios) { usuario ->
                Column(modifier = Modifier.padding()) {
                    Text(text = "---------------------------------------------------------------------------------")
                    Text(text = "Nombre: ${usuario.nombre} ${usuario.apellido}")
                    Text(text = "RUT: ${usuario.rut}")
                    Text(text = "Correo: ${usuario.correo}")
                    Text(text = "Dirección: ${usuario.direccion}")
                    Text(text = "Teléfono: ${usuario.telefono}")
                    Text(text = "---------------------------------------------------------------------------------")
                }
            }
        }
    }
}

