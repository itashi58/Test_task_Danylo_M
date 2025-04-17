package com.example.testtaskalhilal.domain.usecases

import com.example.testtaskalhilal.data.RatesRepository
import com.example.testtaskalhilal.domain.model.Rate
import com.example.testtaskalhilal.domain.model.RatesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class FavoritesUseCase @Inject constructor(
    private val ratesRepository: RatesRepository
) {
    fun getAllFavorites(): Flow<RatesData> {
        return ratesRepository.getRates().combine(ratesRepository.getFavoriteRates()) {
            ratesData, favoriteRates ->
            val favoriteShortNames = favoriteRates.map { it.shortName }
            ratesData.copy(rates = ratesData.rates.filter { favoriteShortNames.contains(it.shortName) }.map { it.copy(isFavorite = true) })
        }
    }

    suspend fun addToFavorites(rate: Rate) {
        ratesRepository.addToFavorites(rate)
    }

    suspend fun removeFromFavorites(rate: Rate) {
        ratesRepository.removeFromFavorites(rate)
    }
}