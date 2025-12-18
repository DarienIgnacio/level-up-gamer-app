package com.example.level_up_gamer_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer_app.model.Producto
import com.example.level_up_gamer_app.repository.ProductoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CatalogoViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _categoriaSeleccionada = MutableStateFlow("Todas")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada

    val productosFiltrados: StateFlow<List<Producto>> =
        combine(_productos, _categoriaSeleccionada) { productos, categoria ->
            if (categoria == "Todas") {
                productos
            } else {
                productos.filter {
                    it.categoria.equals(categoria, ignoreCase = true)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            try {
                _productos.value = repository.obtenerProductos()
            } catch (e: Exception) {
                _productos.value = emptyList()
            }
        }
    }

    fun seleccionarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }
}
