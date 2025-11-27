package com.example.tcgstore.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun AdminScreen(navController: NavController) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = { navController.navigate("intentos_login") },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Intentos de inicio de sesión")
            }
            Button(onClick = { navController.navigate("usuarios_registrados") },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Usuarios registrados")
            }
            Button(onClick = { navController.navigate("agregar_producto") },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Agregar producto")
            }
            Button(onClick = { navController.navigate("eliminar_producto") },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Eliminar producto")
            }
            Button(onClick = { navController.navigate("home") },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Cerrar Sesión")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminScreenPreview() {
    com.example.tcgstore.ui.theme.TCGStoreTheme {
        AdminScreen(rememberNavController())
    }
}
