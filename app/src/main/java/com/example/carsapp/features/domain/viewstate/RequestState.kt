package com.example.carsapp.features.domain.viewstate

import com.example.carsapp.features.domain.model.CarsModel

sealed class RequestState {
    object Idle : RequestState()
    object Loading : RequestState()
    data class Cars(val cars: CarsModel) : RequestState()
    data class Error(val error: String?) : RequestState()
}