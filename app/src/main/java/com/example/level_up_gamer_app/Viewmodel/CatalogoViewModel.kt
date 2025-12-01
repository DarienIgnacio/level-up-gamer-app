package com.example.level_up_gamer_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer_app.repository.ProductoRepository
import com.example.level_up_gamer_app.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel(
    private val repository: ProductoRepository = ProductoRepository()
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        cargarProductos()
    }

    // 🔹 Cargar productos desde el backend
    fun cargarProductos() {
        viewModelScope.launch {
            _loading.value = true
            val resultado = repository.obtenerProductos()
            _loading.value = false

            if (resultado != null) {
                _productos.value = resultado
            } else {
                _error.value = "Error al cargar productos"
            }
        }
    }

    // 🔹 AGREGAR un nuevo producto al backend
    fun agregarProducto(
        nombre: String,
        descripcion: String,
        precio: Int,
        imagenUrl: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _loading.value = true

            val nuevoProducto = Producto(
                id = 0,
                nombre = nombre,
                descripcion = descripcion,
                precio = precio,
                imagen = imagenUrl
            )

            val success = repository.agregarProducto(nuevoProducto)

            _loading.value = false

            if (success) {
                cargarProductos() // refresca catálogo
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }
}
