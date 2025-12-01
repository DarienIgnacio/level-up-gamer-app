package com.example.level_up_gamer_app.data.remote.dto

data class ProductoDto(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val imagen: String?,
    val categoria: String,
    val stock: Int
)
