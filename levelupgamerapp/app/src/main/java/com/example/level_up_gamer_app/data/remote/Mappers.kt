package com.example.level_up_gamer_app.data.remote

import com.example.level_up_gamer_app.data.remote.dto.ProductDto
import com.example.level_up_gamer_app.data.remote.dto.UsuarioResponse
import com.example.level_up_gamer_app.model.Producto
import com.example.level_up_gamer_app.model.Usuario

// ---------------- PRODUCTOS ----------------
fun ProductDto.toProducto(): Producto =
    Producto(
        id = id ?: 0L,              // 🔧 seguro
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        imagen = imagen,
        categoria = categoria,
        stock = stock
    )

fun Producto.toDto(): ProductDto =
    ProductDto(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        imagen = imagen,
        categoria = categoria,
        stock = stock
    )

// ---------------- USUARIO ----------------
fun UsuarioResponse.toUsuario(): Usuario =
    Usuario(
        id = id,
        nombre = nombre,
        email = email,
        rut = rut,
        esAdmin = esAdmin
    )
