package com.example.level_up_gamer_app.Viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer_app.model.Usuario
import com.example.level_up_gamer_app.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
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

    fun login(email: String, password: String) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            val result = repo.login(email, password)
            result
                .onSuccess {
                    usuarioActual = it
                    loginSuccess = true
                }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }

    fun register(nombre: String, email: String, rut: String, password: String) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            val result = repo.register(nombre, email, rut, password)
            result
                .onSuccess { registerSuccess = true }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }

    fun logout() {
        usuarioActual = null
        loginSuccess = false
    }
}
