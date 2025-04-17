package com.example.testtaskalhilal.data.remote

import com.example.testtaskalhilal.data.remote.dto.RatesResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "8ca380d5b12c425392e04fdb497fc059" // better to replace with local properties api key

interface RatesApiService {

    @GET("latest.json")
    suspend fun getRates(
        @Query("app_id") fromDate: String? = API_KEY
    ): RatesResponse

    @GET("currencies.json")
    suspend fun getCurrencies(
        @Query("app_id") fromDate: String? = API_KEY
    ): Map<String, String>
}