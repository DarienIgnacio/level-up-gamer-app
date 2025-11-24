package com.example.level_up_gamer_app.repository

import android.content.Context
import androidx.compose.foundation.layout.add
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.level_up_gamer_app.Model.Producto
import com.example.level_up_gamer_app.Model.FakeDatabase
class ProductoRepository {
    fun obtenerProductosDesdeAssets(context: Context, filename: String = "productos.json"): List<Producto> {
        return try {
            val json = context.assets.open(filename).bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<Producto>>() {}.type
            Gson().fromJson<List<Producto>>(json, listType) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    fun agregarProducto(producto: Producto) {
        FakeDatabase.agregarProducto(producto)
    }
}