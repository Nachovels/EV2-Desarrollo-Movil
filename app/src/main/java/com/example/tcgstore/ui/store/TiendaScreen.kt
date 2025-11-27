package com.example.tcgstore.ui.store

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tcgstore.data.Producto
import com.example.tcgstore.data.UserStorage
import com.example.tcgstore.data.network.ApiConstants
import com.example.tcgstore.data.network.RetrofitClient
import com.example.tcgstore.data.network.models.ProductResponse
import com.example.tcgstore.data.repository.ApiResult
import com.example.tcgstore.data.repository.ProductRepository
import com.example.tcgstore.ui.cart.CarritoViewModel
import com.example.tcgstore.ui.cart.CarritoViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaScreen(navController: NavController) {
    val context = LocalContext.current
    val userStorage = UserStorage(context)
    val productRepository = remember { ProductRepository(context) }
    val carritoViewModel: CarritoViewModel = viewModel(factory = CarritoViewModelFactory(userStorage))
    val scope = rememberCoroutineScope()

    var productos by remember { mutableStateOf<List<ProductResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Cargar productos al iniciar
    LaunchedEffect(Unit) {
        scope.launch {
            isLoading = true
            when (val result = productRepository.getAllProducts()) {
                is ApiResult.Success -> {
                    productos = result.data
                    isLoading = false
                }
                is ApiResult.Error -> {
                    errorMessage = result.message
                    isLoading = false
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tienda") },
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
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar productos",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = errorMessage ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            scope.launch {
                                isLoading = true
                                errorMessage = null
                                when (val result = productRepository.getAllProducts()) {
                                    is ApiResult.Success -> {
                                        productos = result.data
                                        isLoading = false
                                    }
                                    is ApiResult.Error -> {
                                        errorMessage = result.message
                                        isLoading = false
                                    }
                                    else -> {}
                                }
                            }
                        }) {
                            Text("Reintentar")
                        }
                    }
                }
                productos.isEmpty() -> {
                    Text(
                        text = "No hay productos disponibles",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(productos) { producto ->
                            ProductCard(
                                producto = producto,
                                onAddToCart = {
                                    val productoLocal = Producto(
                                        nombre = producto.nombre,
                                        descripcion = producto.descripcion,
                                        precio = producto.precio,
                                        imageUri = ApiConstants.getImageUrl(producto.imagen)
                                    )
                                    carritoViewModel.agregarAlCarrito(productoLocal)
                                    Toast.makeText(context, "Añadido al carrito", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    producto: ProductResponse,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Construir URL completa de la imagen
            val imageUrl = "http://13.216.97.34:8080${producto.imagen}".replace("//uploads", "/uploads")

            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = producto.nombre,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    producto.oferta?.let { oferta ->
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = oferta.uppercase(),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$${String.format("%,d", producto.precio)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onAddToCart,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar al carrito")
                }
            }
        }
    }
}

// Hacer BASE_URL accesible públicamente
object RetrofitClient {
    const val BASE_URL = "http://10.0.2.2:8080/"
}