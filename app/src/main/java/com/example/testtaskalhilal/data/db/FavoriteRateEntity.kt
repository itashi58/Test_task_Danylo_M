package com.example.testtaskalhilal.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_rates")
data class FavoriteRateEntity(
    @PrimaryKey val shortName: String
)