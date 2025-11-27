package com.example.tcgstore.ui.admin

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tcgstore.data.network.models.ProductResponse
import com.example.tcgstore.data.repository.ApiResult
import com.example.tcgstore.data.repository.ProductRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EliminarProductoScreen(navController: NavController) {
    val context = LocalContext.current
    val productRepository = remember { ProductRepository(context) }
    val scope = rememberCoroutineScope()
    var productsState by remember { mutableStateOf<ApiResult<List<ProductResponse>>>(ApiResult.Loading) }

    fun loadProducts() {
        scope.launch {
            productsState = ApiResult.Loading
            productsState = productRepository.getAllProducts()
        }
    }

    LaunchedEffect(Unit) {
        loadProducts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eliminar Productos") },
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
            when (val state = productsState) {
                is ApiResult.Success -> {
                    val productos = state.data
                    if (productos.isEmpty()) {
                        Text("No hay productos en la tienda.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(productos) { producto ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            // Imagen pequeña
                                            val imageUrl = "http://13.216.97.34:8080${producto.imagen}".replace("//uploads", "/uploads")
                                            Image(
                                                painter = rememberAsyncImagePainter(imageUrl),
                                                contentDescription = producto.nombre,
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .padding(end = 16.dp),
                                                contentScale = ContentScale.Crop
                                            )
                                            
                                            Column {
                                                Text(
                                                    text = producto.nombre,
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Text(
                                                    text = "$${producto.precio}",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }

                                        IconButton(onClick = {
                                            scope.launch {
                                                when (val result = productRepository.deleteProduct(producto.id)) {
                                                    is ApiResult.Success -> {
                                                        Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show()
                                                        loadProducts() // Recargar lista
                                                    }
                                                    is ApiResult.Error -> {
                                                        Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_LONG).show()
                                                    }
                                                    else -> {}
                                                }
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Eliminar producto",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                is ApiResult.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Error: ${state.message}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { loadProducts() }) {
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
