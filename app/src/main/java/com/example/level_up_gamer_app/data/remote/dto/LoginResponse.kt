package com.example.level_up_gamer_app.data.remote.dto

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val nombre: String? = null,
    val email: String? = null,
    val rut: String? = null,
    val esAdmin: Boolean? = null
)