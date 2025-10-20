package com.example.tcgstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tcgstore.ui.admin.AgregarProductoScreen
import com.example.tcgstore.ui.admin.AdminScreen
import com.example.tcgstore.ui.admin.IntentoLoginScreen
import com.example.tcgstore.ui.admin.UsuariosRegistradosScreen
import com.example.tcgstore.ui.configuration.ConfigScreen
import com.example.tcgstore.ui.home.HomeScreen
import com.example.tcgstore.ui.login.LoginScreen
import com.example.tcgstore.ui.registration.RegistroScreen
import com.example.tcgstore.ui.registration.RegistroExitosoScreen
import com.example.tcgstore.ui.theme.TCGStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            TCGStoreTheme(darkTheme = isDarkMode) {
                AppNavigation(
                    isDarkMode = isDarkMode,
                    onThemeUpdated = { isDarkMode = it }
                )
            }
        }
    }
}

@Composable
fun AppNavigation(isDarkMode: Boolean, onThemeUpdated: (Boolean) -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController = navController) }
        composable("login") { LoginScreen(navController = navController) }
        composable(
            "register",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { RegistroScreen(navController = navController) }
        composable("registration_success") { RegistroExitosoScreen(navController = navController) }
        composable("admin") { AdminScreen(navController = navController) }
        composable("login_attempts") { IntentoLoginScreen(navController = navController) }
        composable("registered_users") { UsuariosRegistradosScreen(navController = navController) }
        composable("add_product") { AgregarProductoScreen(navController = navController) }
        composable("config") {
            ConfigScreen(
                navController = navController,
                isDarkMode = isDarkMode,
                onThemeUpdated = onThemeUpdated
            )
        }
    }
}
