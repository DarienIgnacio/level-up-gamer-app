package com.example.level_up_gamer_app.data.remote.dto

data class UsuarioResponse(
    val id: Long,
    val nombre: String,
    val email: String,
    val rut: String,
    val esAdmin: Boolean
)
