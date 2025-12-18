package com.example.level_up_gamer_app.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.level_up_gamer_app.model.Producto
import com.example.level_up_gamer_app.viewmodel.CarritoViewModel
import com.example.level_up_gamer_app.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ProductDetailScreen(
    producto: Producto,
    navController: NavController,
    carritoViewModel: CarritoViewModel,
    catalogoViewModel: CatalogoViewModel
) {
    // ==============================
    // PRODUCTO REACTIVO (CLAVE)
    // ==============================
    val productos by catalogoViewModel.productos.collectAsState()

    val productoActual = productos.firstOrNull { it.id == producto.id } ?: producto

    // ==============================
    // REFRESCAR CATÁLOGO TRAS COMPRA
    // ==============================
    LaunchedEffect(Unit) {
        carritoViewModel.eventoCompraExitosa.collect {
            catalogoViewModel.cargarProductos()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ==============================
            // IMAGEN
            // ==============================
            AsyncImage(
                model = productoActual.imagen,
                contentDescription = productoActual.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ==============================
            // NOMBRE
            // ==============================
            Text(
                text = productoActual.nombre,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            // ==============================
            // PRECIO
            // ==============================
            Text(
                text = "CLP ${productoActual.precio}",
                fontSize = 22.sp,
                color = Color(0xFF673AB7),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ==============================
            // STOCK ANIMADO (VERDE / ROJO)
            // ==============================
            AnimatedContent(
                targetState = productoActual.stock,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "stock_animation"
            ) { stockAnimado ->

                Text(
                    text = if (stockAnimado > 0)
                        "Stock disponible: $stockAnimado"
                    else
                        "Sin stock",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (stockAnimado > 0)
                        Color(0xFF4CAF50)
                    else
                        Color(0xFFD32F2F)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==============================
            // RATING
            // ==============================
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Text(
                text = "4.8/5 (simulado)",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ==============================
            // DESCRIPCIÓN
            // ==============================
            Text(
                text = "Descripción",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = productoActual.descripcion,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // ==============================
            // BOTÓN AGREGAR AL CARRITO
            // ==============================
            Button(
                onClick = {
                    carritoViewModel.agregarProducto(productoActual)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = productoActual.stock > 0,
                contentPadding = PaddingValues(14.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = if (productoActual.stock > 0)
                        "Agregar al carrito"
                    else
                        "Sin stock",
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
