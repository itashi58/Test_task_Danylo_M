package com.example.testtaskalhilal.data

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.testtaskalhilal.data.db.FavoriteRateEntity
import com.example.testtaskalhilal.data.db.FavoriteRatesDao
import com.example.testtaskalhilal.data.db.RateEntity
import com.example.testtaskalhilal.data.db.RatesDao
import com.example.testtaskalhilal.data.remote.RatesApiService
import com.example.testtaskalhilal.data.remote.dto.RatesResponse
import com.example.testtaskalhilal.domain.model.Rate
import com.example.testtaskalhilal.domain.model.RatesData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

private const val PREF_LAST_SYNC_TIME = "last_sync_time"
private const val DATE_FORMAT_PATTERN = "HH:mm:ss dd-MM"

@Singleton
class RatesRepository @Inject constructor(
    private val api: RatesApiService,
    private val ratesDao: RatesDao,
    private val favoriteRatesDao: FavoriteRatesDao,
    private val sharedPreferences: SharedPreferences,
    private val ratesDataFactory: RatesDataFactory
) {
    @SuppressLint("DefaultLocale")
    fun getRates(): Flow<RatesData> = flow {
        coroutineScope {
            while (true) {
                val updateRequestTime = SimpleDateFormat(
                    DATE_FORMAT_PATTERN,
                    Locale.getDefault()
                ).format(System.currentTimeMillis())

                val ratesDef = async {
                    try {
                        Result.success(api.getRates())
                    } catch (e: Exception) {
                        Log.e("RatesRepository", "Error fetching rates: ${e.message}")
                        Result.failure<RatesResponse>(e)
                    }
                }
                val currenciesDef = async {
                    try {
                        Result.success(api.getCurrencies())
                    } catch (e: Exception) {
                        Log.e("RatesRepository", "Error fetching rates: ${e.message}")
                        Result.failure<Map<String, String>>(e)
                    }
                }
                val ratesResp = ratesDef.await().getOrNull()
                val currenciesResp = currenciesDef.await().getOrNull()

                if (ratesResp == null || currenciesResp == null) {
                    emit(getCachedRates())
                    break  // Stop network requests if there's an error
                }

                val ratesData = ratesDataFactory.createRatesDataNetwork(
                    ratesResp,
                    currenciesResp,
                    updateRequestTime
                )
                saveRatesData(ratesData)

                emit(ratesData) // Emit the latest rates
                delay(5000) // Wait for 5 seconds before the next API request
                Log.d("RatesRepository", "Fetching rates from API")
            }
        }
    }

    fun getFavoriteRates(): Flow<List<FavoriteRateEntity>> = favoriteRatesDao.getAllRatesFlow()

    suspend fun addToFavorites(rate: Rate) {
        favoriteRatesDao.insertRate(FavoriteRateEntity(rate.shortName))
    }

    suspend fun removeFromFavorites(rate: Rate) {
        favoriteRatesDao.deleteRate(FavoriteRateEntity(rate.shortName))
    }

    private suspend fun getCachedRates(): RatesData =
        ratesDataFactory.createRatesDataLocal(
            ratesDao.getAllRates(),
            sharedPreferences.getString(PREF_LAST_SYNC_TIME, "") ?: ""
        )

    private suspend fun saveRatesData(ratesData: RatesData) {
        sharedPreferences.edit {
            putString(PREF_LAST_SYNC_TIME, ratesData.lastUpdatedTime)
        }
        ratesDao.insertRates(ratesData.rates.map { RateEntity(it.shortName, it.name, it.value) })
    }

}