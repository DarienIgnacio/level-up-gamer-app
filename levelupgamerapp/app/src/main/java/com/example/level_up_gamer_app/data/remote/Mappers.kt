package com.example.level_up_gamer_app.data.remote

import com.example.level_up_gamer_app.Model.Producto
import com.example.level_up_gamer_app.data.remote.dto.ProductoDto

fun ProductoDto.toProducto(): Producto {
    val imagenUrl = if (imagen != null) {
        // Tu backend expone las imágenes en /uploads
        "http://192.168.X.X:8080/uploads/$imagen"
    } else null

    return Producto(
        id = id.toInt(),
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        imagen = imagenUrl ?: "",
        categoria = categoria,
        stock = stock
    )
}
