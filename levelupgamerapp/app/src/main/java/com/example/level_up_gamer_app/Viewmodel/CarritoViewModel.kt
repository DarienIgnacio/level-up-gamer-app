package com.example.level_up_gamer_app.Viewmodel

import androidx.lifecycle.ViewModel
import com.example.level_up_gamer_app.Model.CarritoItem
import com.example.level_up_gamer_app.Model.FakeDatabase
import com.example.level_up_gamer_app.Model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CarritoViewModel : ViewModel() {

    // Flujo de estado para la lista del carrito (privado y mutable)
    private val _carrito = MutableStateFlow<List<CarritoItem>>(emptyList())
    // Flujo de estado público e inmutable para que la UI observe
    val carrito: StateFlow<List<CarritoItem>> = _carrito.asStateFlow()

    // Flujo de estado para el total del carrito
    private val _total = MutableStateFlow(0)
    val total: StateFlow<Int> = _total.asStateFlow()

    // Mensaje para notificaciones (ej. Snackbar)
    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    init {
        // Carga el estado inicial del carrito cuando se crea el ViewModel
        actualizarEstadoCarrito()
    }

    // Función central para actualizar los flujos de estado desde la base de datos
    private fun actualizarEstadoCarrito() {
        _carrito.value = FakeDatabase.obtenerCarrito()
        _total.value = FakeDatabase.calcularTotal()
    }

    fun agregarAlCarrito(producto: Producto, cantidad: Int = 1) {
        FakeDatabase.agregarAlCarrito(producto, cantidad)
        actualizarEstadoCarrito() // Notifica a la UI del cambio
        _mensaje.value = "${producto.nombre} agregado al carrito"
    }

    fun actualizarCantidad(productoId: Int, nuevaCantidad: Int) {
        FakeDatabase.actualizarCantidad(productoId, nuevaCantidad)
        actualizarEstadoCarrito() // Notifica a la UI del cambio
    }

    fun eliminarDelCarrito(productoId: Int) {
        FakeDatabase.eliminarDelCarrito(productoId)
        actualizarEstadoCarrito() // Notifica a la UI del cambio
        _mensaje.value = "Producto eliminado del carrito"
    }

    fun limpiarCarrito() {
        FakeDatabase.limpiarCarrito()
        actualizarEstadoCarrito() // Notifica a la UI del cambio
    }


    fun realizarCompra(): Int? { // Cambia el tipo de retorno a Int? (entero nulable)
        val carritoActual = _carrito.value
        val sinStock = carritoActual.any { it.cantidad > it.producto.stock }

        return if (sinStock) {
            _mensaje.value = "Error: Algunos productos no tienen stock suficiente"
            null // Devuelve null si la compra falla
        } else {
            val totalCompra = _total.value // Guarda el total ANTES de limpiar
            limpiarCarrito() // Ahora sí, limpia el carrito
            _mensaje.value = "Compra realizada exitosamente"
            totalCompra // Devuelve el total guardado
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = ""
    }
}