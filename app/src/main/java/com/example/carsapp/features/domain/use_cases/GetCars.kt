package com.example.carsapp.features.domain.use_cases

import com.example.carsapp.features.domain.model.CarsModel
import com.example.carsapp.features.domain.repo.RequestRepo
import retrofit2.Response

class GetCars(private val requestRepo: RequestRepo) {
    suspend operator fun invoke(page:Int): Response<CarsModel> {
        return if (page > 0)
            requestRepo.requestCars(page)
        else
            Response.success(404,CarsModel())
    }
}