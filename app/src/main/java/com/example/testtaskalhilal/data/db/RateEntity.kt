package com.example.testtaskalhilal.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rates")
data class RateEntity(
    @PrimaryKey val shortName: String,
    val name: String,
    val value: String
)