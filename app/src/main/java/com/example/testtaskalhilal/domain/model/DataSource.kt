package com.example.testtaskalhilal.domain.model

sealed class DataSource {
    object Network : DataSource()
    object Local : DataSource()
}