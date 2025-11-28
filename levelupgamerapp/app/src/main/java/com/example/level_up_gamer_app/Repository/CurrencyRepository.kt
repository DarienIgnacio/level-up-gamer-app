package com.example.level_up_gamer_app.repository

import com.example.level_up_gamer_app.data.remote.RetrofitClient

class CurrencyRepository {

    suspend fun usdToClp(amountUsd: Double): Int {
        return try {
            val response = RetrofitClient.currencyApi.convertCurrency(
                from = "USD",
                to = "CLP",
                amount = amountUsd
            )

            if (response.success) {
                response.result.toInt()
            } else {
                (amountUsd * 900).toInt() // fallback seguro
            }

        } catch (e: Exception) {
            e.printStackTrace()
            (amountUsd * 900).toInt() // fallback local
        }
    }
}
