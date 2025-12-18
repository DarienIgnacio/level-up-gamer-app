package com.example.level_up_gamer_app.model

data class Usuario(
    val id: Long,
    val nombre: String,
    val email: String,
    val rut: String,
    val esAdmin: Boolean
)
