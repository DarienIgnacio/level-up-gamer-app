package com.example.level_up_gamer_app.Model

data class Usuario(
    val nombre: String,
    val email: String,
    val rut: String,
    val password: String,
    val esAdmin: Boolean = false
)