package com.example.level_up_gamer_app.repository

import android.util.Log
import com.example.level_up_gamer_app.data.remote.ApiService
import com.example.level_up_gamer_app.data.remote.RetrofitClient
import com.example.level_up_gamer_app.data.remote.toUsuario
import com.example.level_up_gamer_app.data.remote.dto.LoginRequest
import com.example.level_up_gamer_app.data.remote.dto.RegisterRequest
import com.example.level_up_gamer_app.model.Usuario

class AuthRepository(
    private val api: ApiService = RetrofitClient.apiService
) {

    suspend fun login(email: String, password: String): Usuario? {
        val response = api.login(LoginRequest(email, password))

        return if (response.isSuccessful && response.body() != null) {
            response.body()!!.toUsuario()
        } else null
    }

    // En tu archivo AuthRepository.kt
    suspend fun register(nombre: String, email: String, rut: String, password: String): Result<Unit> {
        return try {
            // 1. Crear el usuario en Firebase Authentication
            val authResult = com.google.firebase.Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                // 2. Guardar datos adicionales en Firestore
                val usuario = Usuario(id = firebaseUser.uid, nombre = nombre, email = email, rut = rut)
                com.google.firebase.Firebase.firestore.collection("usuarios").document(firebaseUser.uid).set(usuario).await()
                Result.success(Unit) // ¡ÉXITO!
            } else {
                // Si Firebase no devuelve un usuario, es un fallo
                Result.failure(Exception("No se pudo obtener el usuario de Firebase."))
            }
        } catch (e: Exception) {
            // ¡ESTA ES LA PARTE MÁS IMPORTANTE!
            // Atrapa CUALQUIER error (red, contraseña débil, email duplicado, etc.)
            // y lo convierte en un Result.failure para que el ViewModel lo reciba.
            Log.e("AuthRepository", "Fallo en el registro: ${e.message}")
            Result.failure(e)
        }
    }

}
