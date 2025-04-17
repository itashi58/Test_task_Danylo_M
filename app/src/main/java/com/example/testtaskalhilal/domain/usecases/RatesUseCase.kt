package com.example.testtaskalhilal.domain.usecases

import com.example.testtaskalhilal.data.RatesRepository
import com.example.testtaskalhilal.domain.model.RatesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class RatesUseCase @Inject constructor(
    private val ratesRepository: RatesRepository
) {
    fun getRates(): Flow<RatesData> {
        return ratesRepository.getRates().combine(ratesRepository.getFavoriteRates()) {
                ratesData, favoriteRates ->
            val favoriteShortNames = favoriteRates.map { it.shortName }
            ratesData.copy(rates = ratesData.rates.map { it.copy(isFavorite = favoriteShortNames.contains(it.shortName)) })
        }
    }
}