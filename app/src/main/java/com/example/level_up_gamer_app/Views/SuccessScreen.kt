package com.example.level_up_gamer_app.Views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SuccessScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("¡Compra realizada con éxito!")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController.navigate("catalog") {
                    popUpTo("catalog") { inclusive = false }
                }
            }) {
                Text("Volver al catálogo")
            }
        }
    }
}