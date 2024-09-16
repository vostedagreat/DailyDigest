package com.example.dailydigest.remote.network

import domain.models.Results

class ApiHelper {
        suspend fun <T : Any> safeApiCall(apiCall: suspend () -> T): Results<T> {
                return try {
                        val result = apiCall.invoke()
                        Results.success(result)
                } catch (throwable: Throwable) {
                        throwable.printStackTrace()
                        Results.error("Server error ${throwable.message}")
                }
        }

        fun safeApicall(function: () -> Unit) {

        }

        data class ErrorResponse(val message: String)

}