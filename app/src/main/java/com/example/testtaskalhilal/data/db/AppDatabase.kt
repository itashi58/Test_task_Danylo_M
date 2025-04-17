package com.example.testtaskalhilal.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RateEntity::class, FavoriteRateEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rateDao(): RatesDao
    abstract fun favoriteRateDao(): FavoriteRatesDao
}