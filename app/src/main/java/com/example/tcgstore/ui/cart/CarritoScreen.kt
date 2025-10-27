package com.example.tcgstore.ui.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tcgstore.data.UserStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(navController: NavController) {
    val context = LocalContext.current
    val userStorage = UserStorage(context)
    val carritoViewModel: CarritoViewModel = viewModel(factory = CarritoViewModelFactory(userStorage))
    val itemsCarrito by carritoViewModel.itemsCarrito.collectAsState()

    val precioTotal = itemsCarrito.sumOf { it.producto.precio * it.cantidad }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito") },
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
                .padding(16.dp)
        ) {
            if (itemsCarrito.isEmpty()) {
                Text(text = "El carrito está vacío", fontSize = 18.sp)
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(itemsCarrito) { itemCarrito ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = itemCarrito.producto.nombre, fontWeight = FontWeight.Bold)
                                Text(text = "$${itemCarrito.producto.precio}")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { carritoViewModel.disminuirCantidad(itemCarrito) }) {
                                    Text(text = "-")
                                }
                                Text(text = "${itemCarrito.cantidad}")
                                IconButton(onClick = { carritoViewModel.aumentarCantidad(itemCarrito) }) {
                                    Text(text = "+")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(16.dp))
                Text(text = "Total: $$precioTotal", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.padding(8.dp))
                Button(
                    onClick = { carritoViewModel.borrarCarrito() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Vaciar carrito")
                }
            }
        }
    }
}
