package com.example.level_up_gamer_app.Views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_up_gamer_app.Viewmodel.CarritoViewModel
import com.example.level_up_gamer_app.model.CarritoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel
) {
    val items by carritoViewModel.items.collectAsState()

    LaunchedEffect(Unit) {
        carritoViewModel.refrescar()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Carrito de compras") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Tu carrito está vacío", modifier = Modifier.align(Alignment.Center))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(items) { item ->
                        CarritoItemRow(item, carritoViewModel)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val total = carritoViewModel.total()

                Text("Total: $${total}", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        carritoViewModel.vaciar()
                        navController.navigate("success")
                    }
                ) {
                    Text("Finalizar compra")
                }
            }
        }
    }
}

@Composable
fun CarritoItemRow(
    item: CarritoItem,
    carritoViewModel: CarritoViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Precio: $${item.producto.precio}")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { carritoViewModel.quitar(item.producto) }) {
                    Text("-")
                }
                Text("${item.cantidad}")
                TextButton(onClick = { carritoViewModel.agregar(item.producto) }) {
                    Text("+")
                }
            }
        }
    }
}
