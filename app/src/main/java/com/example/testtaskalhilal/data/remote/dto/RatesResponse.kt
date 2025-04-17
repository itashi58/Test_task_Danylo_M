package com.example.testtaskalhilal.data.remote.dto

data class RatesResponse(
    val disclaimer: String?,
    val license: String?,
    val timestamp: Long?,
    val base: String?,
    val rates: Map<String?, Double?>
)
