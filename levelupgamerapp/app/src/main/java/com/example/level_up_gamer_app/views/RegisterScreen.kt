package com.example.level_up_gamer_app.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_app.viewmodel.AuthViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.level_up_gamer_app.utils.Validators

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    // Estados de campos
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estados de errores por campo
    var nombreError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var rutError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val isLoading = authViewModel.isLoading
    val errorGeneral = authViewModel.errorMessage
    val registroExitoso = authViewModel.registerSuccess

    // Navegar cuando registro sea exitoso
    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    // VALIDACIÓN GLOBAL
    fun validarCampos(): Boolean {
        var esValido = true

        if (nombre.isBlank()) {
            nombreError = "El nombre no puede estar vacío"
            esValido = false
        } else nombreError = null

        if (!Validators.isValidEmail(email)) {
            emailError = "Correo inválido"
            esValido = false
        } else emailError = null

        if (!Validators.isValidRUT(rut)) {
            rutError = "RUT inválido"
            esValido = false
        } else rutError = null

        if (!Validators.isValidPassword(password)) {
            passwordError = "La contraseña debe tener al menos 6 caracteres"
            esValido = false
        } else passwordError = null

        return esValido
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Regístrate", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))

            // NOMBRE
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it; nombreError = null },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = nombreError != null
            )
            if (nombreError != null) {
                Text(nombreError!!, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = null },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null
            )
            if (emailError != null) {
                Text(emailError!!, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // RUT
            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it; rutError = null },
                label = { Text("RUT") },
                modifier = Modifier.fillMaxWidth(),
                isError = rutError != null
            )
            if (rutError != null) {
                Text(rutError!!, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // CONTRASEÑA
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError != null
            )
            if (passwordError != null) {
                Text(passwordError!!, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // error del servidor
            if (errorGeneral != null) {
                Text(errorGeneral, color = Color.Red)
                Spacer(modifier = Modifier.height(10.dp))
            }

            Button(
                onClick = {
                    if (validarCampos()) {
                        authViewModel.register(nombre, email, rut, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Registrando..." else "Crear Cuenta")
            }
        }
    }
}

