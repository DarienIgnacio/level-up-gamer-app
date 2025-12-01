package com.example.level_up_gamer_app.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {

    // API gratuita: https://exchangerate.host
    @GET("convert")
    suspend fun convertCurrency(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ): CurrencyResponse
}

data class CurrencyResponse(
    val success: Boolean,
    val result: Double
)
