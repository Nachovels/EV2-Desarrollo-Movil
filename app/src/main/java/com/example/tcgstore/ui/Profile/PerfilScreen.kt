package com.example.tcgstore.ui.profile

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.tcgstore.R
import com.example.tcgstore.data.UserStorage
import com.example.tcgstore.ui.theme.TCGStoreTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navController: NavController) {
    val context = LocalContext.current
    val userStorage = UserStorage(context)

    val emailUsuarioLogueado by userStorage.emailUsuarioLogueadoFlow.collectAsState(initial = null)
    val usuarios by userStorage.usuariosFlow.collectAsState(initial = emptyList())

    val usuarioActual = emailUsuarioLogueado?.let { correo ->
        usuarios.find { it.correo == correo }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
            if (usuarioActual != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    val imageUri = usuarioActual.photoUri?.let { Uri.parse(it) }
                    val painter = if (imageUri != null) {
                        rememberAsyncImagePainter(model = imageUri)
                    } else {
                        painterResource(id = R.drawable.tcgstore)
                    }

                    Image(
                        painter = painter,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )

                    ProfileInfoRow(label = "Nombre", value = usuarioActual.nombre)
                    ProfileInfoRow(label = "Apellido", value = usuarioActual.apellido)
                    ProfileInfoRow(label = "RUT", value = usuarioActual.rut)
                    ProfileInfoRow(label = "Correo Electrónico", value = usuarioActual.correo)
                    ProfileInfoRow(label = "Dirección", value = usuarioActual.direccion)
                    ProfileInfoRow(label = "Teléfono", value = usuarioActual.telefono)

                    Spacer(modifier = Modifier.weight(1f))

                    Button(onClick = { navController.navigate("editarPerfil") }) {
                        Text("Editar Perfil")
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    TCGStoreTheme {
        PerfilScreen(navController = rememberNavController())
    }
}
