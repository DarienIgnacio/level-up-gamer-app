package com.example.level_up_gamer_app.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.level_up_gamer_app.Model.Producto
import com.example.level_up_gamer_app.Viewmodel.CatalogoViewModel
import com.example.level_up_gamer_app.Viewmodel.CarritoViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavHostController,
    catalogoViewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel,
    onProductClick: (Producto) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        catalogoViewModel.cargarProductos(context)
    }

    val productos by catalogoViewModel.productos.collectAsState()
    val isLoading by catalogoViewModel.loading.collectAsState()
    val textoBusqueda by catalogoViewModel.textoBusqueda.collectAsState()
    // Observa el estado del carrito para el badge
    val carrito by carritoViewModel.carrito.collectAsState()
    val cantidadItemsCarrito = carrito.sumOf { it.cantidad }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo Gamer") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    BadgedBox(
                        badge = {
                            if (cantidadItemsCarrito > 0) {
                                Badge { Text(cantidadItemsCarrito.toString()) }
                            }
                        }
                    ) {
                        IconButton(onClick = { navController.navigate("cart") }) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Carrito de compras"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchBar(
                textoBusqueda = textoBusqueda,
                onTextoCambiado = { nuevoTexto ->
                    catalogoViewModel.actualizarTextoBusqueda(nuevoTexto)
                }
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val productosFiltrados = productos.filter {
                    it.nombre.contains(textoBusqueda, ignoreCase = true) ||
                            it.descripcion.contains(textoBusqueda, ignoreCase = true)
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(productosFiltrados) { producto ->
                        ProductoCard(
                            producto = producto,
                            onClick = { onProductClick(producto) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    textoBusqueda: String,
    onTextoCambiado: (String) -> Unit
) {
    OutlinedTextField(
        value = textoBusqueda,
        onValueChange = onTextoCambiado,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        label = { Text("Buscar productos...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Icono de búsqueda") },
        singleLine = true,
        shape = MaterialTheme.shapes.extraLarge
    )
}

@Composable
fun ProductoCard(
    producto: Producto,
    onClick: () -> Unit
) {
    // FORMATO DE PRECIO SEGURO Y UNIVERSAL
    val formattedPrice = String.format(Locale.US,  "$${producto.precio}")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            AsyncImage(
                model = producto.imagen,
                contentDescription = "Imagen de ${producto.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedPrice, // Usa el precio formateado de forma segura
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
