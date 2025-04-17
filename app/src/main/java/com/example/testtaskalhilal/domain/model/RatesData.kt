package com.example.testtaskalhilal.domain.model

data class RatesData(
    val rates: List<Rate>,
    val lastUpdatedTime: String,
    val dataSource: DataSource
)