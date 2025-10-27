package com.example.tcgstore.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tcgstore.R

@Composable
fun WelcomeScreen(navController: NavController) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.tcgstore),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 32.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { navController.navigate("tienda") },
                shape = RoundedCornerShape(12.dp)) {
                Text(text = "Ir a la tienda")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("perfil") },
                shape = RoundedCornerShape(12.dp)) {
                Text(text = "Mi perfil")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("pedidos") },
                shape = RoundedCornerShape(12.dp)) {
                Text(text = "Mis pedidos")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("carrito") },
                shape = RoundedCornerShape(12.dp)) {
                Text(text = "Carrito")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("home") },
                shape = RoundedCornerShape(12.dp)) {
                Text(text = "Cerrar Sesión")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    com.example.tcgstore.ui.theme.TCGStoreTheme {
        WelcomeScreen(rememberNavController())
    }
}
