package com.example.carsapp.features.data.data_source

import com.example.carsapp.features.domain.model.CarsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRepository {

    @GET("cars")
    suspend fun getCarsAsync(@Query("page") page:Int): Response<CarsModel>
}