package com.example.level_up_gamer_app.Model

import android.content.Context
import com.example.level_up_gamer_app.repository.ProductoRepository
import com.example.level_up_gamer_app.utils.Validators

object FakeDatabase {
    private val usuarios = mutableListOf<Usuario>()
    private val productos = mutableListOf<Producto>()
    private val carrito = mutableListOf<CarritoItem>()

    // Este método es crucial. Se llamará desde MainActivity una sola vez.
    fun inicializar(context: Context) {
        if (usuarios.isEmpty()) {
            usuarios.add(Usuario(
                nombre = "Admin Gamer",
                email = "admin@tiendagamer.com",
                rut = "12345678-9",
                password = "admin123",
                esAdmin = true
            ))
        }
    }

    // Autenticación
    fun registrar(usuario: Usuario): Boolean {
        if (usuarios.any { it.email == usuario.email || it.rut == usuario.rut }) return false
        if (!Validators.isValidRUT(usuario.rut)) return false
        usuarios.add(usuario)
        return true
    }

    fun login(email: String, password: String): Usuario? {
        return usuarios.find { it.email == email && it.password == password }
    }

    // Productos
    fun obtenerProductos(): List<Producto> = productos
    fun obtenerProducto(id: Int): Producto? = productos.find { it.id == id }
    fun agregarProducto(producto: Producto) {
        val maxId = productos.maxOfOrNull { it.id } ?: 0
        val nuevoProductoConId = producto.copy(id = maxId + 1)
        productos.add(nuevoProductoConId)
    }

    // Carrito
    fun agregarAlCarrito(producto: Producto, cantidad: Int = 1) {
        val itemExistente = carrito.find { it.producto.id == producto.id }
        if (itemExistente != null) {
            carrito[carrito.indexOf(itemExistente)] = itemExistente.copy(cantidad = itemExistente.cantidad + cantidad)
        } else {
            carrito.add(CarritoItem(producto, cantidad))
        }
    }

    fun obtenerCarrito(): List<CarritoItem> = carrito.toList()
    fun actualizarCantidad(productoId: Int, nuevaCantidad: Int) {
        val item = carrito.find { it.producto.id == productoId }
        if (item != null) {
            if (nuevaCantidad <= 0) {
                carrito.remove(item)
            } else {
                carrito[carrito.indexOf(item)] = item.copy(cantidad = nuevaCantidad)
            }
        }
    }

    fun eliminarDelCarrito(productoId: Int) {
        carrito.removeAll { it.producto.id == productoId }
    }

    fun limpiarCarrito() {
        carrito.clear()
    }

    fun calcularTotal(): Int = carrito.sumOf { it.producto.precio * it.cantidad }
}