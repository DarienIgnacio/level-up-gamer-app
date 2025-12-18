package com.example.level_up_gamer_app.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_up_gamer_app.viewmodel.CarritoViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel
) {
    val carrito by carritoViewModel.carritoItems.collectAsState()
    val compraExitosa by carritoViewModel.compraExitosa.collectAsState()
    val errorCompra by carritoViewModel.errorCompra.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("catalogo") }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver al catálogo"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // ===========================
            // LISTA DE PRODUCTOS
            // ===========================
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                items(
                    items = carrito,
                    key = { it.producto.id ?: 0L }
                ) { item ->
                    CarritoItemRow(
                        item = item,
                        onIncrement = {
                            item.producto.id?.let { id ->
                                carritoViewModel.incrementar(id)
                            }
                        },
                        onDecrement = {
                            item.producto.id?.let { id ->
                                carritoViewModel.decrementar(id)
                            }
                        },
                        onDelete = {
                            item.producto.id?.let { id ->
                                carritoViewModel.eliminar(id)
                            }
                        }
                    )
                }
            }

            val subtotal by carritoViewModel.subtotal.collectAsState()
            val iva by carritoViewModel.iva.collectAsState()
            val total by carritoViewModel.total.collectAsState()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Subtotal: CLP $subtotal")
                    Text("IVA (19%): CLP $iva")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        "Total: CLP $total",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // ===========================
            // BOTÓN PAGAR
            // ===========================
            Button(
                onClick = { carritoViewModel.realizarCompra() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Pagar")
            }
        }

        // ===========================
        // DIÁLOGO COMPRA EXITOSA
        // ===========================
        if (compraExitosa == true) {
            AlertDialog(
                onDismissRequest = { carritoViewModel.limpiarEstadoCompra() },
                confirmButton = {
                    TextButton(onClick = {
                        carritoViewModel.limpiarEstadoCompra()
                        navController.navigate("catalogo") {
                            popUpTo("carrito") { inclusive = true }
                        }
                    }) {
                        Text("Aceptar")
                    }
                },
                title = { Text("¡Compra realizada!") },
                text = { Text("Tu compra fue procesada correctamente.") }
            )
        }

        // ===========================
        // DIÁLOGO ERROR
        // ===========================
        if (errorCompra != null) {
            AlertDialog(
                onDismissRequest = { carritoViewModel.limpiarEstadoCompra() },
                confirmButton = {
                    TextButton(onClick = { carritoViewModel.limpiarEstadoCompra() }) {
                        Text("Aceptar")
                    }
                },
                title = { Text("Error") },
                text = { Text(errorCompra ?: "Error desconocido") }
            )
        }
    }
}
