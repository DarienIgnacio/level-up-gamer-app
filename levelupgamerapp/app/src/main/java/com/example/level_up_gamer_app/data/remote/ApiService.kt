package com.example.level_up_gamer_app.data.remote

import com.example.level_up_gamer_app.data.remote.dto.ProductoDto
import retrofit2.http.GET

interface ApiService {

    @GET("api/productos")
    suspend fun getProductos(): List<ProductoDto>
}

