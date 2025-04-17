package com.example.testtaskalhilal.domain.model

data class Rate(
    val shortName: String,
    val name: String,
    val value: String,
    val isFavorite: Boolean = false
)