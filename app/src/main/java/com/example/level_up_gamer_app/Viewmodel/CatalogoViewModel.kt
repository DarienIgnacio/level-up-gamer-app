package com.example.level_up_gamer_app.Viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer_app.Model.Producto // Keep this
import com.example.level_up_gamer_app.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel(
    // El repositorio se puede inyectar para facilitar las pruebas, pero crearlo por defecto está bien para empezar.
    private val repo: ProductoRepository = ProductoRepository()
) : ViewModel() {

    // _productos es el estado interno, mutable.
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    // productos es el estado público, inmutable, que la UI observará.
    val productos: StateFlow<List<Producto>> = _productos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Si quieres manejar categorías, este es el lugar correcto.
    private val _categorias = MutableStateFlow<List<String>>(emptyList())
    val categorias: StateFlow<List<String>> = _categorias
    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda

    fun actualizarTextoBusqueda(nuevoTexto: String) {
        _textoBusqueda.value = nuevoTexto
    }
    // Función para ser llamada por la UI.
    fun cargarProductos(context: Context) {
        // Evita recargar si ya hay productos.
        if (_productos.value.isNotEmpty()) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val listaProductos = repo.obtenerProductosDesdeAssets(context)
                _productos.value = listaProductos
                // Extraer categorías de la lista de productos
                // Asumo que tu clase Producto tiene un campo `categoria`
                // Si no es así, puedes quitar esta línea o agregar el campo.
                // _categorias.value = listaProductos.map { it.categoria }.distinct().sorted()
            } catch (e: Exception) {
                // Manejar errores, por ejemplo, si no se encuentra el archivo.
                // Podrías exponer un StateFlow de error a la UI.
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    // Esta función ahora busca directamente en el estado del ViewModel.
    fun buscarProductoPorId(id: Int): Producto? {
        return _productos.value.find { it.id == id }
    }

    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            // Delega la tarea al repositorio
            repo.agregarProducto(producto)
            val maxId = _productos.value.maxOfOrNull { it.id } ?: 0
            _productos.value = _productos.value + producto.copy(id = maxId + 1)
        }
    }

}
