package com.example.testtaskalhilal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testtaskalhilal.data.RatesDataFactory
import com.example.testtaskalhilal.domain.model.Rate
import com.example.testtaskalhilal.domain.model.RatesData
import com.example.testtaskalhilal.domain.usecases.FavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val favoritesUseCase: FavoritesUseCase,
    private val ratesDataFactory: RatesDataFactory
) : ViewModel() {

    private val _favoriteRates = MutableStateFlow(ratesDataFactory.defaultRatesData())
    val favoriteRates: StateFlow<RatesData> = _favoriteRates

    private var fetchProcess = viewModelScope.launch {
        favoritesUseCase.getAllFavorites().collectLatest { ratesData ->
            _favoriteRates.value = ratesData
        }
    }

    fun restartFetchRates() {
        fetchProcess.cancel()
        fetchProcess = viewModelScope.launch {
            favoritesUseCase.getAllFavorites().collectLatest { ratesData ->
                _favoriteRates.value = ratesData
            }
        }
    }

    fun stopFetchRates() {
        fetchProcess.cancel()
    }

    fun toggleFavorite(rate: Rate) {
        if (rate.isFavorite) {
            deleteFromFavorites(rate)
        } else {
            addToFavorites(rate)
        }
    }

    private fun deleteFromFavorites(rate: Rate) =
        viewModelScope.launch { favoritesUseCase.removeFromFavorites(rate) }


    private fun addToFavorites(rate: Rate) =
        viewModelScope.launch { favoritesUseCase.addToFavorites(rate) }
}