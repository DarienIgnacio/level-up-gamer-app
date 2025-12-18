package com.example.level_up_gamer_app.repository

import com.example.level_up_gamer_app.data.remote.dto.ProductDto
import com.example.level_up_gamer_app.model.CarritoItem

/**
 * Repositorio simple en memoria.
 * No se usa aún para persistir el carrito,
 * pero queda preparado por si después quieres guardar en DataStore o BD local.
 */
class CarritoRepository {

    // Lista interna del carrito (no Compose State aquí)
    private val carrito = mutableListOf<CarritoItem>()

    fun obtenerCarrito(): List<CarritoItem> {
        return carrito.toList()
    }

    fun agregarProducto(producto: ProductDto) {
        val existente = carrito.find { it.producto.id == producto.id }

        if (existente != null) {
            existente.cantidad++
        } else {
            carrito.add(CarritoItem(producto, 1))
        }
    }

    fun quitarProducto(id: Long) {
        carrito.removeAll { it.producto.id == id }
    }

    fun incrementar(id: Long) {
        carrito.find { it.producto.id == id }?.let { it.cantidad++ }
    }

    fun decrementar(id: Long) {
        carrito.find { it.producto.id == id }?.let {
            if (it.cantidad > 1) it.cantidad--
        }
    }

    fun vaciar() {
        carrito.clear()
    }
}
