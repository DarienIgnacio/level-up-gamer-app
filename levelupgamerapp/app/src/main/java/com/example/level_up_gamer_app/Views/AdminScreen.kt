package com.example.level_up_gamer_app.Views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_up_gamer_app.viewmodel.CatalogoViewModel
import com.example.level_up_gamer_app.model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    navController: NavController,
    catalogoViewModel: CatalogoViewModel
) {
    val productos by catalogoViewModel.productos.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Panel Admin") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(productos) { producto ->
                AdminProductItem(producto)
            }
        }
    }
}

@Composable
fun AdminProductItem(producto: Producto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Precio: $${producto.precio}")
            Spacer(modifier = Modifier.height(4.dp))
            Text("Stock: ${producto.stock}")
        }
    }
}
