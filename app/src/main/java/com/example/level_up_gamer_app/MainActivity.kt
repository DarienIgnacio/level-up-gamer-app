package com.example.level_up_gamer_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.level_up_gamer_app.Model.FakeDatabase
import com.example.level_up_gamer_app.Viewmodel.AuthViewModel
import com.example.level_up_gamer_app.Viewmodel.CarritoViewModel
import com.example.level_up_gamer_app.Viewmodel.CatalogoViewModel
import com.example.level_up_gamer_app.views.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa la base de datos falsa con el contexto de la aplicación.
        FakeDatabase.inicializar(this)

        setContent {
            val navController = rememberNavController()
            val authViewModel = remember { AuthViewModel() }
            val catalogoViewModel = remember { CatalogoViewModel() }
            val carritoViewModel = remember { CarritoViewModel() }

            NavHost(
                navController = navController,
                startDestination = "auth_graph"
            ) {
                // Grafo de Autenticación
                navigation(startDestination = "login", route = "auth_graph") {
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
                }

                // Grafo principal de la app
                composable("catalog") {
                    CatalogScreen(
                        navController = navController,
                        catalogoViewModel = catalogoViewModel,
                        carritoViewModel = carritoViewModel,
                        onProductClick = { producto ->
                            navController.navigate("productDetail/${producto.id}")
                        }
                    )
                }

                composable("productDetail/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                    val producto = FakeDatabase.obtenerProducto(productId ?: -1)
                    ProductDetailScreen(
                        navController = navController,
                        producto = producto,
                        carritoViewModel = carritoViewModel
                    )
                }

                // Carrito y compras
                composable("cart") {
                    CartScreen(
                        navController = navController,
                        carritoViewModel = carritoViewModel
                    )
                }
                composable(
                    route = "success/{totalAmount}", // 1. Definir el argumento en la ruta
                    arguments = listOf(navArgument("totalAmount") { type =
                        NavType.IntType }) // 2. Especificar que es un Int
                ) { backStackEntry -> // 3. Usar backStackEntry para obtener el argumento
                    val total = backStackEntry.arguments?.getInt("totalAmount") ?: 0 // 4. Leer el valor
                    SuccessScreen(
                        navController = navController,
                        orderNumber = "GAMER-${System.currentTimeMillis()}",
                        total = total
                    )
                }

                composable("error") {
                    ErrorScreen(navController = navController)
                }

                // Admin
                composable("admin") {
                    AdminScreen(
                        navController = navController,
                        catalogoViewModel = catalogoViewModel
                    )
                }
                composable("addProduct") {
                    AddProductScreen(
                        navController = navController,
                        catalogoViewModel = catalogoViewModel
                    )
                }
            }
        }
    }
}