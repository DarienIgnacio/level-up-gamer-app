package com.example.level_up_gamer_app.repository

import com.example.level_up_gamer_app.data.remote.RetrofitClient

class CurrencyRepository {

    private val api = RetrofitClient.currencyApi

    suspend fun convertirADolaresAClp(monto: Double, base: String = "USD"): Int {
        return try {
            val response = api.getRateToClp(base = base)
            val tasa = response.rates["CLP"] ?: 1.0
            (monto * tasa).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            monto.toInt()
        }
    }
}
