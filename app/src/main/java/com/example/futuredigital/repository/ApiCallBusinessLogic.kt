package com.example.futuredigital.repository

import com.example.futuredigital.util.DataState
import com.example.moduleapiservices.models.ModelCurrentTemp
import com.example.moduleapiservices.models.ModelForecastTemp
import com.example.moduleapiservices.utils.ApiServices
import com.example.moduleapiservices.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiCallBusinessLogic
@Inject
constructor(val apiServices: ApiServices) {
    // Api called for current temprature
    suspend fun execute(cityFetchedFormGps: String): Flow<DataState<ModelCurrentTemp>> = flow {
        emit(DataState.Loading)
        try {
            val modelCurrentTemp =
                apiServices.fetchCurrentTemperaturedata(cityFetchedFormGps, Constants.TOKEN)
            emit(DataState.Success(modelCurrentTemp))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    // Api called for getting forecast of  24 hours in every 3 hours
    suspend fun executeToGetForecastTemp(cityFetchedFormGps: String): Flow<DataState<ModelForecastTemp>> =
        flow {
            emit(DataState.Loading)
            try {
                val modelForeCastTemp =
                    apiServices.fetchForecastTemperaturedata(cityFetchedFormGps, Constants.TOKEN)
                emit(DataState.Success(modelForeCastTemp))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }
}