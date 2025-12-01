package com.example.level_up_gamer_app.data.remote

import com.example.level_up_gamer_app.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ---------- AUTH ----------
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UsuarioResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<UsuarioResponse>


    // ---------- PRODUCTOS ----------
    @GET("api/productos")
    suspend fun getProductos(): List<ProductDto>

    @POST("api/productos/simple")
    suspend fun agregarProducto(@Body dto: ProductDto): Response<ProductDto>
}
