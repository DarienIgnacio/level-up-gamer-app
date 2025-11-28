package com.example.level_up_gamer_app.data.remote

data class ProductsResponse(
    val products: List<ProductoDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
