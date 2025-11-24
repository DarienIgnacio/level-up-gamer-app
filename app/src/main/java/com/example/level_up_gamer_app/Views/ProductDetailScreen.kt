package com.example.level_up_gamer_app.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.level_up_gamer_app.Model.Producto
import com.example.level_up_gamer_app.Viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    producto: Producto?,
    carritoViewModel: CarritoViewModel
) {
    val scrollState = rememberScrollState()
    var cantidad by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        producto?.nombre ?: "Detalles",
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            if (producto != null && producto.stock > 0) {
                ExtendedFloatingActionButton(
                    onClick = {
                        repeat(cantidad) {
                            carritoViewModel.agregarAlCarrito(producto)
                        }
                        // Podrías mostrar un Snackbar de confirmación aquí
                    },
                    modifier = Modifier.padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Agregar al carrito")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar $${producto.precio * cantidad}")
                }
            }
        }
    ) { padding ->
        producto?.let { product ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .fillMaxSize()
            ) {
                // Imagen principal del producto
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    AsyncImage(
                        model = product.imagen,
                        contentDescription = product.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )

                    // Badges superpuestos
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = product.categoria,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Surface(
                            color = if (product.stock > 0) MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                            else MaterialTheme.colorScheme.error.copy(alpha = 0.9f),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = if (product.stock > 0) "En stock" else "Agotado",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Información del producto
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Título y precio
                    Column {
                        Text(
                            text = product.nombre,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "$${producto.precio}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Rating y reviews (simulados)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "4.8",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "(128 reviews)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Selector de cantidad (solo si hay stock)
                    if (product.stock > 0) {
                        Column {
                            Text(
                                text = "Cantidad",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { if (cantidad > 1) cantidad-- },
                                    enabled = cantidad > 1
                                ) {
                                    Text("-", style = MaterialTheme.typography.titleLarge)
                                }

                                Text(
                                    text = cantidad.toString(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(40.dp),
                                    textAlign = TextAlign.Center
                                )

                                OutlinedButton(
                                    onClick = {
                                        if (cantidad < product.stock) cantidad++
                                    },
                                    enabled = cantidad < product.stock
                                ) {
                                    Text("+", style = MaterialTheme.typography.titleLarge)
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    text = "Stock: ${product.stock}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Descripción
                    Column {
                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = product.descripcion,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
                        )
                    }

                    // Especificaciones
                    Column {
                        Text(
                            text = "Especificaciones",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            EspecificacionItem(
                                titulo = "Marca",
                                valor = "Gaming Pro Series"
                            )
                            EspecificacionItem(
                                titulo = "Garantía",
                                valor = "2 años"
                            )
                            EspecificacionItem(
                                titulo = "Envío",
                                valor = "Gratis"
                            )
                            EspecificacionItem(
                                titulo = "Disponibilidad",
                                valor = if (product.stock > 0) "Inmediata" else "Agotado",
                                valorColor = if (product.stock > 0) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.error
                            )
                            EspecificacionItem(
                                titulo = "Categoría",
                                valor = product.categoria
                            )
                        }
                    }

                    // Reviews simulados
                    Column {
                        Text(
                            text = "Opiniones de clientes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            ReviewItem(
                                usuario = "Juan Pérez",
                                rating = 5,
                                comentario = "Excelente producto, superó mis expectativas. La calidad es increíble.",
                                fecha = "Hace 2 días"
                            )
                            ReviewItem(
                                usuario = "María García",
                                rating = 4,
                                comentario = "Muy buen producto, funciona perfectamente. La entrega fue rápida.",
                                fecha = "Hace 1 semana"
                            )
                            ReviewItem(
                                usuario = "Carlos López",
                                rating = 5,
                                comentario = "Increíble calidad-precio. Lo recomiendo 100%.",
                                fecha = "Hace 3 días"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp)) // Espacio para el FAB
            }
        } ?: run {
            // Producto no encontrado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Producto no encontrado",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "El producto que buscas no está disponible o ha sido removido.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Volver al catálogo")
                    }
                }
            }
        }
    }
}

@Composable
fun EspecificacionItem(
    titulo: String,
    valor: String,
    valorColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = valorColor,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun ReviewItem(
    usuario: String,
    rating: Int,
    comentario: String,
    fecha: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header del review
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = usuario,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    // Rating con estrellas
                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = if (index < rating) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                Text(
                    text = fecha,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Comentario
            Text(
                text = comentario,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        }
    }
}