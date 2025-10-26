package com.example.tcgstore.ui.store

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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tcgstore.R
import com.example.tcgstore.data.Product
import com.example.tcgstore.data.UserStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(navController: NavController) {
    val context = LocalContext.current
    val userStorage = UserStorage(context)
    val productsFromStorage by userStorage.productsFlow.collectAsState(initial = emptyList())
    val packageName = context.packageName

    // Hardcoded product list using drawable resources
    val hardcodedProducts = listOf(
        Product(
            name = "Magic The Gathering: Murders at Karlov Manor",
            description = "Bundle con cartas Magic The Gathering: Murders at Karlov Manor.",
            price = 78000,
            imageUri = "android.resource://$packageName/${R.drawable.x_magic_the_gathering_murders_at_karlov_manor_bundle8015}"
        ),
        Product(
            name = "One Piece: A Fist of Divine Speed Booster Box",
            description = "Caja de cartas de One Piece edición A Fist of Divine Speed.",
            price = 60000,
            imageUri = "android.resource://$packageName/${R.drawable.x_op11_one_piece_a_fist_of_divine_speed_booster_box5231}"
        ),
        Product(
            name = "Pokémon Paradox Rift Bundle",
            description = "Bundle de cartas Pokémon Paradox Rift.",
            price = 40000,
            imageUri = "android.resource://$packageName/${R.drawable.x_pkm_pr_etb_iv1709}"
        )
    )

    val allProducts = productsFromStorage + hardcodedProducts

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
            items(allProducts) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Image(
                            painter = rememberAsyncImagePainter(product.imageUri),
                            contentDescription = product.name,
                            modifier = Modifier
                                .height(150.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = product.name, fontWeight = FontWeight.Bold)
                            Text(text = product.description)
                            Text(text = "$${product.price}")
                            Button(onClick = { /* TODO: Add to cart */ },
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
