package com.example.level_up_gamer_app.Model

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val imagen: String,
    val categoria: String = "",
    val stock: Int = 0
)