package com.example.tcgstore.ui.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tcgstore.data.network.models.UserListResponse
import com.example.tcgstore.data.repository.ApiResult
import com.example.tcgstore.data.repository.AuthRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosRegistradosScreen(navController: NavController) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    var usersState by remember { mutableStateOf<ApiResult<List<UserListResponse>>>(ApiResult.Loading) }

    LaunchedEffect(Unit) {
        usersState = authRepository.getAllUsers()
    }

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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = usersState) {
                is ApiResult.Success -> {
                    val usuarios = state.data
                    if (usuarios.isEmpty()) {
                        Text("No hay usuarios registrados.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(usuarios) { usuario ->
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = "Nombre: ${usuario.nombreCompleto}", fontWeight = FontWeight.Bold)
                                    Text(text = "Correo: ${usuario.correoElectronico}")
                                    Divider(modifier = Modifier.padding(top = 16.dp))
                                }
                            }
                        }
                    }
                }
                is ApiResult.Error -> {
                    Text(text = "Error: ${state.message}")
                }
                is ApiResult.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
