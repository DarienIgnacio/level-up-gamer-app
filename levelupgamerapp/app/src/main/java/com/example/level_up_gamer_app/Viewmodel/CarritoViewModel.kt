package com.example.level_up_gamer_app.Viewmodel

import androidx.lifecycle.ViewModel
import com.example.level_up_gamer_app.model.CarritoItem
import com.example.level_up_gamer_app.model.Producto
import com.example.level_up_gamer_app.repository.CarritoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CarritoViewModel(
    private val repo: CarritoRepository = CarritoRepository()
) : ViewModel() {

    private val _items = MutableStateFlow<List<CarritoItem>>(emptyList())
    val items: StateFlow<List<CarritoItem>> = _items

    fun refrescar() {
        _items.value = repo.obtenerCarrito()
    }

    fun agregar(producto: Producto) {
        repo.agregarProducto(producto)
        refrescar()
    }

    fun quitar(producto: Producto) {
        repo.quitarProducto(producto)
        refrescar()
    }

    fun vaciar() {
        repo.vaciar()
        refrescar()
    }

    fun total(): Int =
        _items.value.sumOf { it.producto.precio * it.cantidad }
}
