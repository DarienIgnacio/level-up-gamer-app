package com.example.level_up_gamer_app.data.remote

data class ProductoDto(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)

