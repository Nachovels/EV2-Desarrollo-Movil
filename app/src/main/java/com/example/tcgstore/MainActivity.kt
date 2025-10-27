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
import com.example.tcgstore.ui.profile.EditarPerfilScreen
import com.example.tcgstore.ui.admin.AgregarProductoScreen
import com.example.tcgstore.ui.admin.AdminScreen
import com.example.tcgstore.ui.admin.IntentoLoginScreen
import com.example.tcgstore.ui.admin.UsuariosRegistradosScreen
import com.example.tcgstore.ui.cart.CarritoScreen
import com.example.tcgstore.ui.configuration.ConfigScreen
import com.example.tcgstore.ui.home.HomeScreen
import com.example.tcgstore.ui.login.LoginScreen
import com.example.tcgstore.ui.orders.PedidosScreen
import com.example.tcgstore.ui.profile.PerfilScreen
import com.example.tcgstore.ui.registration.RegistroScreen
import com.example.tcgstore.ui.registration.RegistroExitosoScreen
import com.example.tcgstore.ui.store.TiendaScreen
import com.example.tcgstore.ui.theme.TCGStoreTheme
import com.example.tcgstore.ui.welcome.WelcomeScreen

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

        composable("login",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { LoginScreen(navController = navController) }

        composable(
            "registro",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { RegistroScreen(navController = navController) }

        composable("registro_exitoso",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { RegistroExitosoScreen(navController = navController) }

        composable("admin",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { AdminScreen(navController = navController) }

        composable("intentos_login",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { IntentoLoginScreen(navController = navController) }

        composable("usuarios_registrados",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { UsuariosRegistradosScreen(navController = navController) }

        composable("agregar_producto",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { AgregarProductoScreen(navController = navController) }

        composable("welcome",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { WelcomeScreen(navController = navController) }

        composable("perfil",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        )   { PerfilScreen(navController = navController) }

        composable("editarPerfil",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { EditarPerfilScreen(navController = navController) }

        composable("tienda",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { TiendaScreen(navController = navController) }

        composable("pedidos",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { PedidosScreen(navController = navController) }

        composable("carrito",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) { CarritoScreen(navController = navController) }

        composable("config",enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
        ) {
            ConfigScreen(
                navController = navController,
                isDarkMode = isDarkMode,
                onThemeUpdated = onThemeUpdated
            )
        }
    }
}
