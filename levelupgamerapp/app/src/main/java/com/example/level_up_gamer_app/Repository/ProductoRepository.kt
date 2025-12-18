package com.example.level_up_gamer_app.repository

import com.example.level_up_gamer_app.data.remote.ApiService
import com.example.level_up_gamer_app.data.remote.toProducto
import com.example.level_up_gamer_app.data.remote.toDto
import com.example.level_up_gamer_app.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductoRepository(
    private val apiService: ApiService
) {

    // =========================
    // READ: listar productos
    // =========================
    suspend fun obtenerProductos(): List<Producto> = withContext(Dispatchers.IO) {
        val response = apiService.getProductos()
        if (response.isSuccessful) {
            response.body().orEmpty().map { it.toProducto() }
        } else {
            throw Exception("Error al obtener productos: HTTP ${response.code()}")
        }
    }

    // =========================
    // CREATE: agregar producto
    // =========================
    suspend fun agregarProducto(producto: Producto): Producto = withContext(Dispatchers.IO) {
        val response = apiService.agregarProducto(producto.toDto())
        if (response.isSuccessful) {
            response.body()?.toProducto()
                ?: throw Exception("Respuesta vacía al agregar producto")
        } else {
            throw Exception("Error al agregar producto: HTTP ${response.code()}")
        }
    }

    // =========================
    // UPDATE: actualizar producto
    // =========================
    suspend fun actualizarProducto(producto: Producto): Producto = withContext(Dispatchers.IO) {
        val response = apiService.actualizarProducto(
            producto.id,
            producto.toDto()
        )
        if (response.isSuccessful) {
            response.body()?.toProducto()
                ?: throw Exception("Respuesta vacía al actualizar producto")
        } else {
            throw Exception("Error al actualizar producto: HTTP ${response.code()}")
        }
    }

    // =========================
    // DELETE
    // =========================
    suspend fun eliminarProducto(id: Long) = withContext(Dispatchers.IO) {
        val response = apiService.deleteProducto(id)
        if (!response.isSuccessful) {
            throw Exception("Error al eliminar producto: HTTP ${response.code()}")
        }
    }
}
