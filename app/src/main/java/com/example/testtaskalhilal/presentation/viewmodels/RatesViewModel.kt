package com.example.testtaskalhilal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testtaskalhilal.data.RatesDataFactory
import com.example.testtaskalhilal.domain.model.Rate
import com.example.testtaskalhilal.domain.model.RatesData
import com.example.testtaskalhilal.domain.usecases.FavoritesUseCase
import com.example.testtaskalhilal.domain.usecases.RatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatesViewModel @Inject constructor(
    private val ratesUseCase: RatesUseCase,
    private val favoritesUseCase: FavoritesUseCase,
    private val ratesDataFactory: RatesDataFactory
) : ViewModel() {
    private val _rates = MutableStateFlow(ratesDataFactory.defaultRatesData())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val rates: StateFlow<RatesData> = combine(_rates, _searchQuery) { ratesData, query ->
        if (query.isEmpty()) {
            ratesData
        } else {
            ratesData.copy(rates = ratesData.rates.filter {
                it.shortName.contains(query, ignoreCase = true) ||
                        it.name.contains(query, ignoreCase = true)
            })
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ratesDataFactory.defaultRatesData())

    private var fetchProcess = viewModelScope.launch {
        ratesUseCase.getRates().collectLatest { ratesData ->
            _rates.value = ratesData
        }
    }

    fun restartFetchRates() {
        fetchProcess.cancel()
        fetchProcess = viewModelScope.launch {
            ratesUseCase.getRates().collectLatest { ratesData ->
                _rates.value = ratesData
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

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun deleteFromFavorites(rate: Rate) =
        viewModelScope.launch { favoritesUseCase.removeFromFavorites(rate) }

    private fun addToFavorites(rate: Rate) =
        viewModelScope.launch { favoritesUseCase.addToFavorites(rate) }
}