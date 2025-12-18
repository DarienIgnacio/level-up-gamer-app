package com.example.level_up_gamer_app.data.remote

import com.example.level_up_gamer_app.data.remote.dto.LoginRequest
import com.example.level_up_gamer_app.data.remote.dto.RegisterRequest
import com.example.level_up_gamer_app.data.remote.dto.ProductDto
import com.example.level_up_gamer_app.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ======================
    // AUTH
    // ======================
    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<Usuario>

    @POST("/api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Usuario>

    @GET("/api/usuarios")
    suspend fun obtenerUsuarios(): Response<List<Usuario>>

    // ======================
    // PRODUCTOS
    // ======================

    // 👉 Listar productos
    @GET("api/productos")
    suspend fun getProductos(): Response<List<ProductDto>>

    // 👉 Agregar producto (admin)
    @POST("/api/productos")
    suspend fun agregarProducto(
        @Body dto: ProductDto
    ): Response<ProductDto>

    // 👉 Obtener producto por ID
    @GET("/api/productos/{id}")
    suspend fun getProducto(
        @Path("id") id: Long
    ): Response<ProductDto>

    // 👉 Eliminar producto
    @DELETE("/api/productos/{id}")
    suspend fun deleteProducto(
        @Path("id") id: Long
    ): Response<Unit>

    // 👉 ACTUALIZAR STOCK (USADO EN COMPRA)
    @PUT("/api/productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: Long,
        @Body producto: ProductDto
    ): Response<ProductDto>

    @PUT("/api/productos/{id}/disminuir-stock")
    suspend fun disminuirStock(
        @Path("id") id: Long,
        @Query("cantidad") cantidad: Int
    ): retrofit2.Response<ProductDto>

}
