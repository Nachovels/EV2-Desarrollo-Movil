package com.example.tcgstore.ui.configuration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    navController: NavController,
    isDarkMode: Boolean,
    onThemeUpdated: (Boolean) -> Unit,
) {
    var notificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 16.dp)
        ) {
            SectionTitle("Apariencia")
            SettingsRow(title = "Modo Oscuro") {
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onThemeUpdated
                )
            }
            SettingsRow(title = "Tamaño del Texto", showArrow = true) {}

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            SectionTitle("Notificaciones")
            SettingsRow(title = "Recibir notificaciones") {
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            SectionTitle("Información y Soporte")
            SettingsRow(title = "Acerca de", showArrow = true) {}
            SettingsRow(title = "Términos y Condiciones", showArrow = true) {}
            SettingsRow(title = "Política de Privacidad", showArrow = true) {}
            SettingsRow(title = "Ayuda y Soporte", showArrow = true) {}
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsRow(
    title: String,
    showArrow: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null || showArrow) { onClick?.invoke() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(title, modifier = Modifier.weight(1f))
        if (content != null) {
            content()
        }
        if (showArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigScreenPreview() {
    var isDarkMode by remember { mutableStateOf(false) }
    ConfigScreen(
        navController = rememberNavController(),
        isDarkMode = isDarkMode,
        onThemeUpdated = { isDarkMode = it }
    )
}
