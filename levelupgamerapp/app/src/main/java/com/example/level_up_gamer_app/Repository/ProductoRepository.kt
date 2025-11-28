package com.example.level_up_gamer_app.repository

import android.content.Context
import com.example.level_up_gamer_app.Model.Producto
import com.example.level_up_gamer_app.data.local.AppDatabase
import com.example.level_up_gamer_app.data.remote.RetrofitClient
import com.example.level_up_gamer_app.data.remote.toProducto

class ProductoRepository {

    suspend fun obtenerProductosRemotosOCache(context: Context): List<Producto> {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()

        return try {
            val response = RetrofitClient.apiService.getProductos()

            val productos = response.map { it.toProducto() }

            // Guardar en Room
            dao.insertAll(productos)

            productos
        } catch (e: Exception) {
            e.printStackTrace()
            // Si falla, devolver desde Room
            dao.getAll()
        }
    }

    // Admin: agregar producto local (puedes eliminar si ya no usas FakeDatabase)
    fun agregarProducto(producto: Producto) {
        com.example.level_up_gamer_app.Model.FakeDatabase.agregarProducto(producto)
    }
}
