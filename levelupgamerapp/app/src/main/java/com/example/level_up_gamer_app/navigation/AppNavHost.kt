package com.example.level_up_gamer_app.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.level_up_gamer_app.views.*
import com.example.level_up_gamer_app.viewmodel.*

@Composable
fun AppNavHost(
    navController: NavController,
    catalogoViewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel,
    adminProductViewModel: AdminProductViewModel,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = "login"
    ) {

        // =========================
        // LOGIN / REGISTER
        // =========================
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

        // =========================
        // CATÁLOGO
        // =========================
        composable("catalogo") {
            CatalogScreen(
                navController = navController,
                catalogoViewModel = catalogoViewModel
            )
        }

        // =========================
        // CARRITO
        // =========================
        composable("carrito") {
            CarritoScreen(
                navController = navController,
                carritoViewModel = carritoViewModel
            )
        }

        // =========================
        // DETALLE PRODUCTO
        // =========================
        composable(
            route = "productDetail/{productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.LongType }
            )
        ) { backStackEntry ->

            val productId = backStackEntry.arguments?.getLong("productId")
            val productos by catalogoViewModel.productos.collectAsState()

            LaunchedEffect(Unit) {
                if (productos.isEmpty()) {
                    catalogoViewModel.cargarProductos()
                }
            }

            val producto = productos.firstOrNull { it.id == productId }

            if (producto != null) {
                ProductDetailScreen(
                    producto = producto,
                    navController = navController,
                    carritoViewModel = carritoViewModel,
                    catalogoViewModel = catalogoViewModel
                )
            } else {
                // Si entra con id inválido o lista aún vacía, vuelve atrás cuando haya data
                LaunchedEffect(productos) {
                    if (productos.isNotEmpty()) navController.popBackStack()
                }
            }
        }

        // =========================
        // ADMIN (RUTA QUE TU LOGIN USA)
        // =========================
        composable("admin_home") {
            // Si tu “home admin” es esta pantalla, perfecto.
            // Si quieres otra pantalla de admin, cambia aquí.
            AddProductScreen(
                viewModel = adminProductViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // (Opcional) Mantengo esta ruta por si en algún lado navegas a "admin"
        composable("admin") {
            AddProductScreen(
                viewModel = adminProductViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
