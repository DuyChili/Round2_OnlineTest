package com.example.basicmobileapplication.api

import com.example.basicmobileapplication.models.Currency
import com.example.basicmobileapplication.models.ExchangeRates
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitApi {
    @GET("{apiKey}/latest/{base}")
    fun getExchangeRates(
        @Path("apiKey") apiKey: String,
        @Path("base") base: String
    ): Call<ExchangeRates>
    

    @GET("{apiKey}/pair/{baseRate}/{baseResult}/{amount}")
    fun getCurrency(
        @Path("apiKey") apiKey: String,
        @Path("baseRate") baseRate: String,
        @Path("baseResult") baseResult: String,
        @Path("amount") amount: String
    ): Call<Currency>
}
