package com.example.level_up_gamer_app.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.level_up_gamer_app.data.remote.dto.ProductDto

@Composable
fun AddOrEditProductDialog(
    initial: ProductDto?,
    onDismiss: () -> Unit,
    onSave: (ProductDto) -> Unit
) {
    var nombre by remember { mutableStateOf(initial?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(initial?.descripcion ?: "") }
    var precio by remember { mutableStateOf(initial?.precio?.toString() ?: "") }
    var imagen by remember { mutableStateOf(initial?.imagen ?: "") }
    var categoria by remember { mutableStateOf(initial?.categoria ?: "") }
    var stock by remember { mutableStateOf(initial?.stock?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val producto = ProductDto(
                    id = initial?.id,
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio.toIntOrNull() ?: 0,
                    imagen = imagen,
                    categoria = categoria,
                    stock = stock.toIntOrNull() ?: 0
                )
                onSave(producto)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text(if (initial == null) "Agregar Producto" else "Editar Producto") },
        text = {
            Column {
                OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") })
                OutlinedTextField(precio, { precio = it }, label = { Text("Precio") })
                OutlinedTextField(imagen, { imagen = it }, label = { Text("Imagen (URL)") })
                OutlinedTextField(categoria, { categoria = it }, label = { Text("Categoría") })
                OutlinedTextField(stock, { stock = it }, label = { Text("Stock") })
            }
        }
    )
}
