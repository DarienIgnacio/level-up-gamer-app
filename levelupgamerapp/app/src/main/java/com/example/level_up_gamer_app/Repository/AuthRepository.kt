package com.example.level_up_gamer_app.repository

import com.example.level_up_gamer_app.data.remote.ApiService
import com.example.level_up_gamer_app.data.remote.RetrofitClient
import com.example.level_up_gamer_app.data.remote.dto.LoginRequest
import com.example.level_up_gamer_app.data.remote.dto.RegisterRequest
import com.example.level_up_gamer_app.model.Usuario

class AuthRepository(
    private val api: ApiService = RetrofitClient.apiService
) {

    suspend fun login(email: String, password: String): Result<Usuario> {
        val response = api.login(LoginRequest(email, password))

        return when {
            response.isSuccessful && response.body() != null ->
                Result.success(response.body()!!)
            else ->
                Result.failure(Exception("Error de login: ${response.errorBody()?.string()}"))
        }
    }

    suspend fun register(nombre: String, email: String, rut: String, password: String): Result<Usuario> {
        val response = api.register(RegisterRequest(nombre, email, password, rut))

        return when {
            response.isSuccessful && response.body() != null ->
                Result.success(response.body()!!)
            else ->
                Result.failure(Exception("Error al registrar: ${response.errorBody()?.string()}"))
        }
    }
}
