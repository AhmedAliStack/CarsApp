package com.example.carsapp.features.domain.repo

import com.example.carsapp.features.domain.model.CarsModel
import retrofit2.Response

interface RequestRepo {
    suspend fun requestCars(page:Int) : Response<CarsModel>
}