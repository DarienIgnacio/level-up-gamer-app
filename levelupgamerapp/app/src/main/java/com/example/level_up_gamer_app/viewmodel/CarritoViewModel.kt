package com.example.level_up_gamer_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer_app.data.remote.ApiService
import com.example.level_up_gamer_app.data.remote.RetrofitClient
import com.example.level_up_gamer_app.data.remote.dto.ProductDto
import com.example.level_up_gamer_app.model.CarritoItem
import com.example.level_up_gamer_app.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CarritoViewModel(
    private val apiService: ApiService = RetrofitClient.apiService
) : ViewModel() {

    private val _carritoItems = MutableStateFlow<List<CarritoItem>>(emptyList())
    val carritoItems: StateFlow<List<CarritoItem>> = _carritoItems

    private val _eventoCompraExitosa = MutableSharedFlow<Unit>()
    val eventoCompraExitosa = _eventoCompraExitosa.asSharedFlow()


    // =============================
    // TOTALES
    // =============================
    val subtotal: StateFlow<Int> =
        carritoItems.map { lista ->
            lista.sumOf { it.producto.precio * it.cantidad }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    val iva: StateFlow<Int> =
        subtotal.map { (it * 0.19).toInt() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    val total: StateFlow<Int> =
        subtotal.map { sub -> sub + (sub * 0.19).toInt() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    // =============================
    // RESULTADO COMPRA
    // =============================
    private val _compraExitosa = MutableStateFlow<Boolean?>(null)
    val compraExitosa: StateFlow<Boolean?> = _compraExitosa

    private val _errorCompra = MutableStateFlow<String?>(null)
    val errorCompra: StateFlow<String?> = _errorCompra

    fun limpiarEstadoCompra() {
        _compraExitosa.value = null
        _errorCompra.value = null
    }

    // =============================
    // AGREGAR PRODUCTO
    // =============================
    fun agregarProducto(producto: Producto) {
        val dto = producto.toDto()

        val nuevaLista = _carritoItems.value.toMutableList()
        val index = nuevaLista.indexOfFirst { it.producto.id == dto.id }

        if (index >= 0) {
            val item = nuevaLista[index]
            nuevaLista[index] = item.copy(cantidad = item.cantidad + 1)
        } else {
            nuevaLista.add(CarritoItem(dto, 1))
        }

        _carritoItems.value = nuevaLista
    }

    fun incrementar(productoId: Long) {
        _carritoItems.value = _carritoItems.value.map {
            if (it.producto.id == productoId)
                it.copy(cantidad = it.cantidad + 1)
            else it
        }
    }

    fun decrementar(productoId: Long) {
        _carritoItems.value = _carritoItems.value.map {
            if (it.producto.id == productoId && it.cantidad > 1)
                it.copy(cantidad = it.cantidad - 1)
            else it
        }
    }

    fun eliminar(productoId: Long) {
        _carritoItems.value =
            _carritoItems.value.filterNot { it.producto.id == productoId }
    }

    // =============================
    // COMPRA
    // =============================
    fun realizarCompra() {
        viewModelScope.launch {
            try {
                // Validación rápida
                if (_carritoItems.value.isEmpty()) {
                    _compraExitosa.value = false
                    _errorCompra.value = "El carrito está vacío"
                    return@launch
                }

                for (item in _carritoItems.value) {
                    val id = item.producto.id
                    val cantidad = item.cantidad

                    if (id == null) {
                        _compraExitosa.value = false
                        _errorCompra.value = "Producto sin ID"
                        return@launch
                    }

                    val response = apiService.disminuirStock(
                        id = id,
                        cantidad = cantidad
                    )

                    if (!response.isSuccessful) {
                        _compraExitosa.value = false

                        _errorCompra.value = when (response.code()) {
                            400 -> "Stock insuficiente para ${item.producto.nombre}"
                            404 -> "Producto no encontrado: ${item.producto.nombre}"
                            else -> "Error backend: HTTP ${response.code()}"
                        }
                        return@launch
                    }
                }

                // Todo OK
                _carritoItems.value = emptyList()
                _errorCompra.value = null
                _compraExitosa.value = true
                _eventoCompraExitosa.emit(Unit)


            } catch (e: retrofit2.HttpException) {
                _compraExitosa.value = false
                _errorCompra.value = "Error backend: HTTP ${e.code()}"
            } catch (e: Exception) {
                _compraExitosa.value = false
                _errorCompra.value = e.message ?: "Error desconocido"
            }
        }
    }
}

    // EXTENSIÓN
private fun Producto.toDto(): ProductDto =
    ProductDto(id, nombre, descripcion, precio, imagen, categoria, stock)
