package com.example.tcgstore.ui.store

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tcgstore.R
import com.example.tcgstore.data.Producto
import com.example.tcgstore.data.UserStorage
import com.example.tcgstore.ui.cart.CarritoViewModel
import com.example.tcgstore.ui.cart.CarritoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaScreen(navController: NavController) {
    val context = LocalContext.current
    val userStorage = UserStorage(context)
    val carritoViewModel: CarritoViewModel = viewModel(factory = CarritoViewModelFactory(userStorage))
    val productosAlmacenados by userStorage.productosFlow.collectAsState(initial = emptyList())
    val packageName = context.packageName

    val productosHardcodeados = listOf(
        Producto(
            nombre = "Magic The Gathering: Murders at Karlov Manor",
            descripcion = "Bundle con cartas Magic The Gathering: Murders at Karlov Manor.",
            precio = 78000,
            imageUri = "android.resource://$packageName/${R.drawable.x_magic_the_gathering_murders_at_karlov_manor_bundle8015}"
        ),
        Producto(
            nombre = "One Piece: A Fist of Divine Speed Booster Box",
            descripcion = "Caja de cartas de One Piece edición A Fist of Divine Speed.",
            precio = 60000,
            imageUri = "android.resource://$packageName/${R.drawable.x_op11_one_piece_a_fist_of_divine_speed_booster_box5231}"
        ),
        Producto(
            nombre = "Pokémon Paradox Rift Bundle",
            descripcion = "Bundle de cartas Pokémon Paradox Rift.",
            precio = 40000,
            imageUri = "android.resource://$packageName/${R.drawable.x_pkm_pr_etb_iv1709}"
        )
    )

    val todosProductos = productosAlmacenados + productosHardcodeados

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
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(todosProductos) { producto ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Image(
                            painter = rememberAsyncImagePainter(producto.imageUri),
                            contentDescription = producto.nombre,
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = producto.nombre, fontWeight = FontWeight.Bold)
                            Text(text = producto.descripcion)
                            Text(text = "$${producto.precio}")
                            Button(onClick = { 
                                carritoViewModel.agregarAlCarrito(producto)
                                Toast.makeText(context, "Añadido al carrito", Toast.LENGTH_SHORT).show()
                            },
                                shape = RoundedCornerShape(12.dp)) {
                                Text("Agregar al carrito")
                            }
                        }
                    }
                }
            }
        }
    }
}
