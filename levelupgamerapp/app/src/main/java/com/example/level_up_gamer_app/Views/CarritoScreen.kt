package com.example.level_up_gamer_app.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_up_gamer_app.Viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel
) {
    // Observa los StateFlows del ViewModel
    val carrito by carritoViewModel.carrito.collectAsState()
    val total by carritoViewModel.total.collectAsState()

    // Formateador de moneda seguro
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            if (carrito.isNotEmpty()) { // Muestra la barra inferior solo si hay items
                Surface(tonalElevation = 4.dp) { // Añade elevación para separarla del contenido
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total:",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = currencyFormatter.format(total),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                // 1. Llama a la función y guarda el total
                                val totalDeCompra = carritoViewModel.realizarCompra()

                                // 2. Comprueba si la compra fue exitosa (no es null)
                                if (totalDeCompra != null) {
                                    // 3. Navega a la ruta de éxito, pasando el total en la URL
                                    navController.navigate("success/$totalDeCompra")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            enabled = carrito.isNotEmpty()
                        ) {
                            Text("Proceder al Pago")
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (carrito.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.navigate("catalog") }) {
                        Text("Explorar catálogo")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(carrito, key = { it.producto.id }) { item ->
                    CartItemRow(
                        item = item,
                        onQuantityChange = {
                            carritoViewModel.actualizarCantidad(item.producto.id, it)
                        },
                        onRemove = {
                            carritoViewModel.eliminarDelCarrito(item.producto.id)
                        },
                        currencyFormatter = currencyFormatter
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: com.example.level_up_gamer_app.Model.CarritoItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
    currencyFormatter: NumberFormat
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currencyFormatter.format(item.producto.precio),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }

        Divider(modifier = Modifier.padding(horizontal = 16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { onQuantityChange(item.cantidad - 1) },
                    modifier = Modifier.size(40.dp),
                    enabled = item.cantidad > 1,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("-", style = MaterialTheme.typography.titleLarge)
                }
                Text(
                    text = item.cantidad.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                OutlinedButton(
                    onClick = { onQuantityChange(item.cantidad + 1) },
                    modifier = Modifier.size(40.dp),
                    enabled = item.cantidad < item.producto.stock, // Limita al stock
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("+", style = MaterialTheme.typography.titleLarge)
                }
            }
            Text(
                text = currencyFormatter.format(item.producto.precio * item.cantidad),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}