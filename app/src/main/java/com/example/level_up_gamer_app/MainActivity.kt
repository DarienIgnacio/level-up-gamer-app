package com.example.level_up_gamer_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.level_up_gamer_app.Viewmodel.AuthViewModel
import com.example.level_up_gamer_app.Viewmodel.CarritoViewModel
import com.example.level_up_gamer_app.viewmodel.CatalogoViewModel
import com.example.level_up_gamer_app.Views.*
import com.example.level_up_gamer_app.views.AddProductScreen

class MainActivity : ComponentActivity() {

    // ViewModels únicos para toda la Activity
    private val authViewModel: AuthViewModel by viewModels()
    private val catalogoViewModel: CatalogoViewModel by viewModels()
    private val carritoViewModel: CarritoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {

                    // -------- AUTH --------
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            authViewModel = authViewModel
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            navController = navController,
                            authViewModel = authViewModel
                        )
                    }

                    // -------- CATÁLOGO --------
                    composable("catalogo") {
                        CatalogScreen(
                            navController = navController,
                            catalogoViewModel = catalogoViewModel,
                            carritoViewModel = carritoViewModel   // ✅ se pasa el carrito
                        )
                    }

                    // -------- CARRITO --------
                    composable("carrito") {
                        CarritoScreen(
                            navController = navController,
                            carritoViewModel = carritoViewModel   // ✅ se pasa el carrito
                        )
                    }

                    // -------- ADMIN / AGREGAR PRODUCTO --------
                    composable("admin") {
                        AdminScreen(
                            navController = navController,
                            catalogoViewModel = catalogoViewModel
                        )
                    }

                    composable("admin/addProduct") {
                        AddProductScreen(
                            navController = navController,
                            catalogoViewModel = catalogoViewModel,
                            carritoViewModel = carritoViewModel,
                            viewModel = catalogoViewModel
                            // ✅ se pasa el carrito
                        )
                    }

                    // -------- DETALLE PRODUCTO --------
                    composable(
                        route = "productDetail/{id}",
                        arguments = listOf(
                            navArgument("id") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getLong("id") ?: 0L

                        // ✅ Usamos collectAsState en lugar de .value dentro de la composición
                        val productos by catalogoViewModel.productos.collectAsState()
                        val producto = productos.firstOrNull { it.id == id }

                        if (producto != null) {
                            ProductDetailScreen(
                                navController = navController,
                                producto = producto,
                                carritoViewModel = carritoViewModel  // ✅ se pasa el carrito
                            )
                        } else {
                            ErrorScreen(navController)
                        }
                    }

                    // -------- UTILIDAD --------
                    composable("success") {
                        SuccessScreen(navController)
                    }

                    composable("error") {
                        ErrorScreen(navController)
                    }
                }
            }
        }
    }
}
