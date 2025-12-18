package com.example.level_up_gamer_app.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.level_up_gamer_app.data.remote.dto.CurrencyResponse

interface CurrencyApiService {

    // https://api.exchangerate.host/latest?base=USD&symbols=CLP
    @GET("latest")
    suspend fun getRateToClp(
        @Query("base") base: String,
        @Query("symbols") symbols: String = "CLP"
    ): CurrencyResponse
}
