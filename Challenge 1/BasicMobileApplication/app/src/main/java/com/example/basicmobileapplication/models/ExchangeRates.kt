package com.example.basicmobileapplication.models

data class ExchangeRates(
    val base_code: String,
    val conversion_rates: Map<String, Double>
)
