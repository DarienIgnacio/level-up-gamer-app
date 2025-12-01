package com.example.level_up_gamer_app.repository

import com.example.level_up_gamer_app.data.remote.ApiService
import com.example.level_up_gamer_app.data.remote.RetrofitClient
import com.example.level_up_gamer_app.data.remote.toDto
import com.example.level_up_gamer_app.data.remote.toProducto
import com.example.level_up_gamer_app.model.Producto

class ProductoRepository(
    private val api: ApiService = RetrofitClient.apiService
) {

    suspend fun obtenerProductos(): List<Producto> {
        return api.getProductos().map { it.toProducto() }
    }

    suspend fun agregarProducto(producto: Producto): Boolean {
        val dto = producto.toDto()
        val response = api.agregarProducto(dto)
        return response.isSuccessful
    }
}
