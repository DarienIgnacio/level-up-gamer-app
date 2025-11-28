package com.example.level_up_gamer_app.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val imagen: String,
    val categoria: String = "",
    val stock: Int = 0
)

