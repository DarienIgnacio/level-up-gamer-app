package com.example.level_up_gamer_app.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.level_up_gamer_app.data.remote.dto.ProductDto
import com.example.level_up_gamer_app.viewmodel.AdminProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    viewModel: AdminProductViewModel,
    onBack: () -> Unit = {}
) {
    val productos by viewModel.productos.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val editingProducto = remember { mutableStateOf<ProductDto?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar Productos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingProducto.value = null
                    showDialog.value = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(12.dp)
        ) {
            items(
                items = productos,
                key = { it.id ?: 0L }
            ) { producto ->
                AdminProductItem(
                    producto = producto,
                    onEdit = {
                        editingProducto.value = producto
                        showDialog.value = true
                    },
                    onDelete = {
                        producto.id?.let { id ->
                            viewModel.eliminarProducto(id)
                        }
                    }
                )
            }
        }

        if (showDialog.value) {
            AddOrEditProductDialog(
                initial = editingProducto.value,
                onDismiss = { showDialog.value = false },
                onSave = { p ->
                    if (p.id == null) {
                        viewModel.agregarProducto(p)
                    } else {
                        viewModel.actualizarProducto(p)
                    }
                    showDialog.value = false
                }
            )
        }
    }
}
