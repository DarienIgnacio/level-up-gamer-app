package com.example.level_up_gamer_app.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context

object RetrofitClient {

    private const val BASE_URL = "http://23.22.166.178:9090/"
    private const val CURRENCY_BASE_URL = "https://api.exchangerate.host/"

    // Usa 'by lazy' para crear el cliente OkHttp solo cuando se necesite.
    // Este cliente se compartirá entre ambas instancias de Retrofit.
    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    // Crea la instancia de Retrofit para tu backend principal, también con 'lazy'.
    private val backendRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Usa el cliente lazy
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Crea la instancia de Retrofit para la API de divisas, con 'lazy'.
    private val currencyRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(CURRENCY_BASE_URL)
            .client(okHttpClient) // Reutiliza el mismo cliente OkHttp
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Expón tus servicios de API, que también se crearán de forma perezosa.
    val apiService: ApiService by lazy {
        backendRetrofit.create(ApiService::class.java)
    }

    val currencyApi: CurrencyApiService by lazy {
        currencyRetrofit.create(CurrencyApiService::class.java)
    }
}
