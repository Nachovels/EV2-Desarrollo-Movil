package com.example.tcgstore.ui.orders

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.tcgstore.R
import com.example.tcgstore.data.Pedido
import com.example.tcgstore.data.Producto
import com.example.tcgstore.ui.theme.TCGStoreTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreen(navController: NavController) {
    val context = LocalContext.current
    val packageName = context.packageName

    val pedidosHardcodeados = listOf(
        Pedido(
            id = "ORD-101",
            fecha = "2024-05-20",
            total = 78000.0,
            estado = "Entregado",
            productos = listOf(
                Producto("Magic The Gathering: Murders at Karlov Manor", "", 78000, "android.resource://$packageName/${R.drawable.x_magic_the_gathering_murders_at_karlov_manor_bundle8015}")
            )
        ),
        Pedido(
            id = "ORD-102",
            fecha = "2024-05-18",
            total = 100000.0,
            estado = "Entregado",
            productos = listOf(
                Producto("One Piece: A Fist of Divine Speed Booster Box", "", 60000, "android.resource://$packageName/${R.drawable.x_op11_one_piece_a_fist_of_divine_speed_booster_box5231}"),
                Producto("Pokémon Paradox Rift Bundle", "", 40000, "android.resource://$packageName/${R.drawable.x_pkm_pr_etb_iv1709}")
            )
        ),
        Pedido("ORD-103", "2024-05-21", 60000.0, "Enviado", productos = listOf(Producto("One Piece: A Fist of Divine Speed Booster Box", "", 60000, "android.resource://$packageName/${R.drawable.x_op11_one_piece_a_fist_of_divine_speed_booster_box5231}")))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(pedidosHardcodeados) { pedido ->
                PedidoCard(pedido = pedido)
            }
        }
    }
}

@Composable
fun PedidoCard(pedido: Pedido) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Pedido #${pedido.id}", fontWeight = FontWeight.Bold)
                Text(text = pedido.fecha, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Estado: ${pedido.estado}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            Text(text = "Total: $${String.format("%,.0f", pedido.total)}")
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text(text = "Productos:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            pedido.productos.forEach {
                ProductRow(producto = it)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ProductRow(producto: Producto) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberAsyncImagePainter(producto.imageUri),
            contentDescription = producto.nombre,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = producto.nombre, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun PedidoScreenPreview() {
    TCGStoreTheme {
        PedidosScreen(navController = rememberNavController())
    }
}
