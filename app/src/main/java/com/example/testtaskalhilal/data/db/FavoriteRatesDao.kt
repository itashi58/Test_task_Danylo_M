package com.example.testtaskalhilal.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rate: FavoriteRateEntity)

    @Query("SELECT * FROM favorite_rates")
    fun getAllRatesFlow(): Flow<List<FavoriteRateEntity>>

    @Delete
    suspend fun deleteRate(rate: FavoriteRateEntity)
}