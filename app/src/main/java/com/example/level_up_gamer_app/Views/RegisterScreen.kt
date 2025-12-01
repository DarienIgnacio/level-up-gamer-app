package com.example.level_up_gamer_app.Views

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_up_gamer_app.Viewmodel.AuthViewModel
import com.example.level_up_gamer_app.utils.Validators
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Text("Registro", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = rut,
            onValueChange = { rut = it },
            label = { Text("RUT (11.111.111-1)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // VALIDACIONES
                when {
                    nombre.isBlank() ->
                        errorMessage = "Debes ingresar un nombre"

                    !Validators.isValidEmail(email) ->
                        errorMessage = "Correo electrónico inválido"

                    !Validators.isValidRUT(rut) ->
                        errorMessage = "RUT inválido"

                    !Validators.isValidPassword(password) ->
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"

                    else -> {
                        errorMessage = null
                        authViewModel.register(nombre, email, rut, password)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        TextButton(
            onClick = { navController.navigate("login") }
        ) {
            Text("Volver a iniciar sesión")
        }
    }

    // NAVEGACIÓN CUANDO REGISTER SUCCESS
    if (authViewModel.registerSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }
}
