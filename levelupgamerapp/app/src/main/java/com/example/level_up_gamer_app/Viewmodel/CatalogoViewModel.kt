package com.example.level_up_gamer_app.Viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer_app.Model.Producto
import com.example.level_up_gamer_app.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel(
    private val repo: ProductoRepository = ProductoRepository()
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda

    fun actualizarTextoBusqueda(nuevoTexto: String) {
        _textoBusqueda.value = nuevoTexto
    }

    fun cargarProductos(context: Context) {
        // Opcional: evita recargar si ya hay datos
        if (_productos.value.isNotEmpty()) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val listaProductos = repo.obtenerProductosRemotosOCache(context)
                _productos.value = listaProductos
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    fun buscarProductoPorId(id: Int): Producto? {
        return _productos.value.find { it.id == id }
    }

    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            repo.agregarProducto(producto)
            val maxId = _productos.value.maxOfOrNull { it.id } ?: 0
            _productos.value = _productos.value + producto.copy(id = maxId + 1)
        }
    }
}
