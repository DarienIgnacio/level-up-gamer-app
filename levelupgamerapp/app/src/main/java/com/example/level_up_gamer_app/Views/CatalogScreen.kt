package com.example.level_up_gamer_app.Views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_up_gamer_app.Viewmodel.CarritoViewModel
import com.example.level_up_gamer_app.viewmodel.CatalogoViewModel
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavController,
    catalogoViewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel,
) {
    val context = LocalContext.current
    val productos by catalogoViewModel.productos.collectAsState()
    val loading by catalogoViewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        catalogoViewModel.cargarProductos()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo Gamer") },
                actions = {
                    IconButton(onClick = { navController.navigate("cart") }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(productos) { producto ->
                        ProductCard(producto = producto) {
                            navController.navigate("productDetail/${producto.id}")
                        }
                    }
                }
            }
        }
    }
}
