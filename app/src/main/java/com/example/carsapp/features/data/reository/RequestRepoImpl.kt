package com.example.carsapp.features.data.reository

import com.example.carsapp.features.data.data_source.ApiRepository
import com.example.carsapp.features.domain.model.CarsModel
import com.example.carsapp.features.domain.repo.RequestRepo
import retrofit2.Response

class RequestRepoImpl(private val apiRepository: ApiRepository):RequestRepo {
    override suspend fun requestCars(page:Int): Response<CarsModel> {
        return apiRepository.getCarsAsync(page)
    }
}
