package com.example.level_up_gamer_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer_app.model.Usuario
import com.example.level_up_gamer_app.repository.AuthRepository
import kotlinx.coroutines.launch
import android.util.Log

class
AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set

    var loginSuccess by mutableStateOf(false)
        private set

    var registerSuccess by mutableStateOf(false)
        private set

    fun resetLoginState() {
        loginSuccess = false
        errorMessage = null
        // Si tienes otros estados que limpiar, hazlo aquí.
    }

    fun login(email: String, password: String) {
        isLoading = true
        errorMessage = null
        // Reseteamos el estado de éxito al iniciar un nuevo login
        loginSuccess = false

        viewModelScope.launch {        try {
            // AHORA LA LLAMADA AL REPOSITORIO ESTÁ PROTEGIDA
            val result = repo.login(email, password)

            // Este código solo se ejecutará si repo.login devuelve un Result exitosamente
            result.onSuccess { usuario ->
                usuarioActual = usuario
                loginSuccess = true
            }.onFailure { error ->
                errorMessage = error.message ?: "Error desconocido en login"
            }

        } catch (e: Exception) {
            // ¡AQUÍ ESTÁ LA MAGIA!
            // Si repo.login lanza cualquier excepción (red, Gson, etc.), la atrapamos aquí.
            // Esto evita que la aplicación se cierre.
            errorMessage = "Ocurrió un error: ${e.message}"
            // Opcional: Imprime el error completo en Logcat para depurar
            Log.e("AuthViewModel", "Excepción en el login", e)
        } finally {
            // Este bloque se ejecuta siempre, haya éxito o error.
            // Asegura que el indicador de carga se oculte.
            isLoading = false
        }
        }
    }


    fun register(nombre: String, email: String, rut: String, password: String) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val result = repo.register(nombre, email, rut, password)

            result.onSuccess {
                registerSuccess = true
            }.onFailure { error ->
                errorMessage = error.message ?: "Error en registro"
            }

            isLoading = false
        }
    }


    fun logout() {
        usuarioActual = null
        loginSuccess = false
    }
}
