package com.example.level_up_gamer_app.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_up_gamer_app.Viewmodel.CarritoViewModel
import com.example.level_up_gamer_app.model.Producto
import com.example.level_up_gamer_app.viewmodel.CatalogoViewModel
import com.example.level_up_gamer_app.utils.Validators

@Composable
fun AddProductScreen(
    navController: NavController,
    viewModel: CatalogoViewModel,
    catalogoViewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel
) {

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(20.dp)) {

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = precio,
            onValueChange = { if (Validators.isValidPrice(it)) precio = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = imagenUrl,
            onValueChange = { if (Validators.isValidUrl(it)) imagenUrl = it },
            label = { Text("URL de imagen") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val producto = Producto(
                    id = 0L,
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio.toInt(),
                    imagen = imagenUrl,
                    categoria = "General",
                    stock = 10
                )

                viewModel.agregarProducto(producto.nombre, producto.descripcion, producto.precio, producto.imagen,) { success ->
                    if (success) navController.navigate("success")
                    else navController.navigate("error")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Producto")
        }
    }
}
