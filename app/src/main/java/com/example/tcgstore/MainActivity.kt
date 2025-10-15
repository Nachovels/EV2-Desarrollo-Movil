package com.example.tcgstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tcgstore.ui.home.HomeScreen
import com.example.tcgstore.ui.registration.RegistrationScreen
import com.example.tcgstore.ui.theme.TCGStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TCGStoreTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController = navController) }
        composable(
            "register",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { RegistrationScreen(navController = navController) }
    }
}