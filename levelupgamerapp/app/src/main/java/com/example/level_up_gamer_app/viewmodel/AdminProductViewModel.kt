package com.example.level_up_gamer_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer_app.data.remote.ApiService
import com.example.level_up_gamer_app.data.remote.RetrofitClient
import com.example.level_up_gamer_app.data.remote.dto.ProductDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminProductViewModel(
    private val apiService: ApiService = RetrofitClient.apiService
) : ViewModel() {

    private val _productos = MutableStateFlow<List<ProductDto>>(emptyList())
    val productos: StateFlow<List<ProductDto>> = _productos

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            val response = apiService.getProductos()
            if (response.isSuccessful) {
                _productos.value = response.body().orEmpty()
            }
        }
    }

    fun agregarProducto(producto: ProductDto) {
        viewModelScope.launch {
            val response = apiService.agregarProducto(producto)
            if (response.isSuccessful) {
                cargarProductos()
            }
        }
    }

    fun actualizarProducto(producto: ProductDto) {
        val id = producto.id ?: return
        viewModelScope.launch {
            val response = apiService.actualizarProducto(id, producto)
            if (response.isSuccessful) {
                cargarProductos()
            }
        }
    }

    fun eliminarProducto(id: Long) {
        viewModelScope.launch {
            val response = apiService.deleteProducto(id)
            if (response.isSuccessful) {
                cargarProductos()
            }
        }
    }
}
