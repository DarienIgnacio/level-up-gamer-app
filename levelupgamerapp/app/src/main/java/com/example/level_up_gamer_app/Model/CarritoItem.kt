package com.example.level_up_gamer_app.model

import com.example.level_up_gamer_app.data.remote.dto.ProductDto

data class CarritoItem(
    val producto: ProductDto,
    var cantidad: Int
)
