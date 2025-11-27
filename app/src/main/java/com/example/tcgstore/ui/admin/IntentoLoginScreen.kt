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
import com.example.tcgstore.data.IntentoLogin
import com.example.tcgstore.data.repository.ApiResult
import com.example.tcgstore.data.repository.AuthRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntentoLoginScreen(navController: NavController) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    var attemptsState by remember { mutableStateOf<ApiResult<List<IntentoLogin>>>(ApiResult.Loading) }

    LaunchedEffect(Unit) {
        attemptsState = authRepository.getLoginAttempts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Intentos de inicio de sesión") },
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
            when (val state = attemptsState) {
                is ApiResult.Success -> {
                    val intentos = state.data
                    if (intentos.isEmpty()) {
                        Text("No hay intentos de login registrados.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(intentos) { intento ->
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = "Correo: ${intento.correo}", fontWeight = FontWeight.Bold)
                                    Text(text = "Estado: ${if (intento.exito) "Exitoso" else "Fallido"}")
                                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                                    val date = Date(intento.horario)
                                    Text(text = "Fecha: ${sdf.format(date)}")
                                    Divider(modifier = Modifier.padding(top = 16.dp))
                                }
                            }
                        }
                    }
                }
                is ApiResult.Error -> {
                    Text(text = state.message)
                }
                is ApiResult.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
