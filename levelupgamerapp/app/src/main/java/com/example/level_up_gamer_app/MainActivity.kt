package com.example.level_up_gamer_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.level_up_gamer_app.data.remote.RetrofitClient
import com.example.level_up_gamer_app.navigation.AppNavHost
import com.example.level_up_gamer_app.ui.theme.LevelUpGamerAppTheme
import com.example.level_up_gamer_app.viewmodel.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LevelUpGamerAppTheme {

                val navController = rememberNavController()

                val authViewModel: AuthViewModel = viewModel()

                // ✅ API REAL
                val apiService = RetrofitClient.apiService

                // ✅ ViewModels con Factory correcta
                val catalogoViewModel: CatalogoViewModel = viewModel(
                    factory = CatalogoViewModelFactory(apiService)
                )

                val carritoViewModel: CarritoViewModel = viewModel(
                    factory = CarritoViewModelFactory(apiService)
                )

                val adminProductViewModel: AdminProductViewModel = viewModel(
                    factory = AdminProductViewModelFactory(apiService)
                )

                AppNavHost(
                    navController = navController,
                    catalogoViewModel = catalogoViewModel,
                    carritoViewModel = carritoViewModel,
                    adminProductViewModel = adminProductViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}
