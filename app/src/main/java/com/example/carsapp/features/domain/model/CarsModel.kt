package com.example.carsapp.features.domain.model


import com.google.gson.annotations.SerializedName

data class CarsModel(
    @SerializedName("data")
    val `data`: ArrayList<Data> = arrayListOf(),
    @SerializedName("status")
    val status: Int = 0
) {
    data class Data(
        @SerializedName("brand")
        val brand: String = "",
        @SerializedName("constractionYear")
        val constractionYear: String?,
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("isUsed")
        val isUsed: Boolean = false
    )
}