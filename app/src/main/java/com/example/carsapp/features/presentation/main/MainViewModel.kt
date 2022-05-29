package com.example.carsapp.features.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsapp.features.domain.use_cases.RequestUseCase
import com.example.carsapp.features.domain.viewstate.RequestState
import com.google.gson.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val requestUseCases: RequestUseCase): ViewModel(){
    private val _requestState = MutableStateFlow<RequestState>(RequestState.Idle)
    val requestState : StateFlow<RequestState> = _requestState

    fun getCars(page:Int){
        viewModelScope.launch {
            _requestState.value = RequestState.Loading
            Log.d("Page",page.toString())
            try {
                val response = requestUseCases.getCars(page)
                if(response.isSuccessful)
                    response.body()?.let { _requestState.value = RequestState.Cars(it) }
                else {
                    val msg = JsonParser().parse(
                        response.errorBody()?.string()
                    ).asJsonObject.get("message").asString
                    _requestState.value = RequestState.Error(msg)
                }
            }catch (e: Exception){
                _requestState.value = RequestState.Error(e.localizedMessage)
            }
        }
    }
}