package com.example.tcgstore.ui.admin

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tcgstore.data.network.models.UserListResponse
import com.example.tcgstore.data.repository.ApiResult
import com.example.tcgstore.data.repository.AuthRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosRegistradosScreen(navController: NavController) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    val scope = rememberCoroutineScope()
    var usersState by remember { mutableStateOf<ApiResult<List<UserListResponse>>>(ApiResult.Loading) }

    fun loadUsers() {
        scope.launch {
            usersState = ApiResult.Loading
            usersState = authRepository.getAllUsers()
        }
    }

    LaunchedEffect(Unit) {
        loadUsers()
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
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "Nombre: ${usuario.nombreCompleto}", fontWeight = FontWeight.Bold)
                                        Text(text = "Correo: ${usuario.correoElectronico}")
                                        Text(text = "Rol: ${usuario.role}", style = MaterialTheme.typography.bodySmall)
                                    }
                                    IconButton(onClick = {
                                        scope.launch {
                                            val result = authRepository.deleteUser(usuario.id)
                                            if (result is ApiResult.Success) {
                                                Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                                                loadUsers() // Recargar lista
                                            } else if (result is ApiResult.Error) {
                                                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar usuario",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                                Divider()
                            }
                        }
                    }
                }
                is ApiResult.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Error: ${state.message}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { loadUsers() }) {
                            Text("Reintentar")
                        }
                    }
                }
                is ApiResult.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
