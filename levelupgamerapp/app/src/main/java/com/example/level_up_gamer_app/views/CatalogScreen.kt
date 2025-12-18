package com.example.level_up_gamer_app.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_up_gamer_app.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavController,
    catalogoViewModel: CatalogoViewModel
) {
    val productos by catalogoViewModel.productos.collectAsState()

    // cargar al entrar
    LaunchedEffect(Unit) {
        if (productos.isEmpty()) catalogoViewModel.cargarProductos()
    }

    // categorías disponibles
    val categorias = remember(productos) {
        listOf("TODAS") + productos.mapNotNull { it.categoria?.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    var categoriaSeleccionada by remember { mutableStateOf("TODAS") }

    val productosFiltrados = remember(productos, categoriaSeleccionada) {
        if (categoriaSeleccionada == "TODAS") productos
        else productos.filter { (it.categoria ?: "").equals(categoriaSeleccionada, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo") },
                actions = {
                    IconButton(onClick = { navController.navigate("carrito") }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Filtro categorías
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = categoriaSeleccionada,
                    onValueChange = {},
                    label = { Text("Filtrar por categoría") }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                categoriaSeleccionada = cat
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productosFiltrados) { producto ->
                    ProductCard(
                        producto = producto,
                        onClick = {
                            val id = producto.id ?: return@ProductCard
                            navController.navigate("productDetail/$id")
                        }
                    )
                }
            }
        }
    }
}
