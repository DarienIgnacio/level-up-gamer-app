package com.example.level_up_gamer_app.Viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.level_up_gamer_app.Model.FakeDatabase
import com.example.level_up_gamer_app.Model.Usuario
import com.example.level_up_gamer_app.utils.Validators

class AuthViewModel : ViewModel() {
    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf<Usuario?>(null)

    fun registrar(
        nombre: String,
        email: String,
        rut: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        // Validaciones
        if (nombre.isBlank() || email.isBlank() || rut.isBlank() || password.isBlank()) {
            mensaje.value = "Todos los campos son obligatorios"
            return false
        }

        if (!Validators.isValidEmail(email)) {
            mensaje.value = "Email inválido"
            return false
        }

        if (!Validators.isValidRUT(rut)) {
            mensaje.value = "RUT inválido"
            return false
        }

        if (!Validators.isValidPassword(password)) {
            mensaje.value = "La contraseña debe tener al menos 6 caracteres"
            return false
        }

        if (password != confirmPassword) {
            mensaje.value = "Las contraseñas no coinciden"
            return false
        }

        val nuevoUsuario = Usuario(nombre, email, rut, password)
        return if (FakeDatabase.registrar(nuevoUsuario)) {
            mensaje.value = "Registro exitoso ✅"
            true
        } else {
            mensaje.value = "El usuario ya existe ❌"
            false
        }
    }

    fun login(email: String, password: String): Boolean {
        val usuario = FakeDatabase.login(email, password)
        return if (usuario != null) {
            usuarioActual.value = usuario
            mensaje.value = "Inicio de sesión exitoso 🎉"
            true
        } else {
            mensaje.value = "Credenciales inválidas ❌"
            false
        }
    }

    fun logout() {
        usuarioActual.value = null
        mensaje.value = "Sesión cerrada"
    }
}