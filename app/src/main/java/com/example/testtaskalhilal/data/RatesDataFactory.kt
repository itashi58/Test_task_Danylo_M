package com.example.testtaskalhilal.data

import com.example.testtaskalhilal.data.db.RateEntity
import com.example.testtaskalhilal.data.remote.dto.RatesResponse
import com.example.testtaskalhilal.domain.model.DataSource
import com.example.testtaskalhilal.domain.model.Rate
import com.example.testtaskalhilal.domain.model.RatesData
import javax.inject.Inject

class RatesDataFactory @Inject constructor() {
    fun createRatesDataNetwork(ratesResponse: RatesResponse, currenciesResp: Map<String, String>, updateRequestTime: String, ) : RatesData {
        val rates = ratesResponse.rates.map {
            val shortName = it.key ?: ""
            Rate(
                shortName,
                currenciesResp[shortName] ?: "",
                 it.value.toString()
            )
        }

        return RatesData(rates, updateRequestTime, DataSource.Network)
    }

    fun createRatesDataLocal(ratesResponse: List<RateEntity>, updateRequestTime: String, ) : RatesData {
        val rates = ratesResponse.map {
            Rate(
                it.shortName,
                it.name,
                it.value,
            )
        }
        return RatesData(rates, updateRequestTime, DataSource.Local)
    }

    fun defaultRatesData() : RatesData = RatesData(listOf(), "", DataSource.Local)
}