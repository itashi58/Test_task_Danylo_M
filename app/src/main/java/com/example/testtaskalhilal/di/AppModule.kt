package com.example.testtaskalhilal.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.testtaskalhilal.data.remote.RatesApiService
import com.example.testtaskalhilal.data.db.AppDatabase
import com.example.testtaskalhilal.data.db.FavoriteRatesDao
import com.example.testtaskalhilal.data.db.RatesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val RATES_SERVICE_BASE_URL = "https://openexchangerates.org/api/"
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideBaseUrl(): String = RATES_SERVICE_BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): RatesApiService = retrofit.create(RatesApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRatesDao(database: AppDatabase): RatesDao {
        return database.rateDao()
    }

    @Provides
    @Singleton
    fun provideFavoritesRatesDao(database: AppDatabase): FavoriteRatesDao {
        return database.favoriteRateDao()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
}