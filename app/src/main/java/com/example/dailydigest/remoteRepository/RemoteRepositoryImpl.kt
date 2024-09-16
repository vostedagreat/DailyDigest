package com.example.dailydigest.remoteRepository

import Utils.Utils.Companion.decodeExceptionMessage
import com.example.dailydigest.dto.ErrorDTO
import com.example.dailydigest.dto.NewsDTO
import com.example.dailydigest.remote.network.ApiHelper
import com.example.dailydigest.remote.network.Http
import domain.models.Results
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteRepositoryImpl (
    val apiHelper: ApiHelper,
) :RemoteRepository {

    override suspend fun getNews(): Flow<Results<NewsDTO>> {
        return flow {
            try {
                val response = Http().client.get("/v2/top-headlines?country=us&category=technology&apiKey=8032b416514643b9a80f4779961cdc71")

                if (response.status != HttpStatusCode.OK) {
                    val apiResponse = apiHelper.safeApiCall { response.body<ErrorDTO>() }
                    emit(Results.error(apiResponse.data?.message ?: "Error getting News"))
                } else {
                    val apiResponse =
                        apiHelper.safeApiCall { response.body<NewsDTO>() }
                    emit(apiResponse)
                }
            } catch (e: Exception) {
                emit(Results.error(decodeExceptionMessage(e)))
            }
        }.flowOn(Dispatchers.Default)
    }

}