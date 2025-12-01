package com.example.level_up_gamer_app.repository

import com.example.level_up_gamer_app.model.CarritoItem
import com.example.level_up_gamer_app.model.Producto

class CarritoRepository {

    private val _items = mutableListOf<CarritoItem>()

    fun obtenerCarrito(): List<CarritoItem> = _items.toList()

    fun agregarProducto(producto: Producto) {
        val existente = _items.indexOfFirst { it.producto.id == producto.id }
        if (existente >= 0) {
            val item = _items[existente]
            _items[existente] = item.copy(cantidad = item.cantidad + 1)
        } else {
            _items.add(CarritoItem(producto, 1))
        }
    }

    fun quitarProducto(producto: Producto) {
        val existente = _items.indexOfFirst { it.producto.id == producto.id }
        if (existente >= 0) {
            val item = _items[existente]
            if (item.cantidad > 1) {
                _items[existente] = item.copy(cantidad = item.cantidad - 1)
            } else {
                _items.removeAt(existente)
            }
        }
    }

    fun vaciar() {
        _items.clear()
    }
}
