package com.example.level_up_gamer_app.data.remote.dto

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val rut: String,
    val password: String
)