package com.example.dailydigest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailydigest.dto.Articles
import com.example.dailydigest.dto.NewsDTO
import com.example.dailydigest.remoteRepository.RemoteRepository
import com.example.dailydigest.local.Database.DatabaseDataSource
import domain.models.ResultStatus
import domain.models.Results
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class NewsViewModel(
    private val remoteRepository: RemoteRepository,
    private val databaseDataSource: DatabaseDataSource
) : ViewModel() {

    val fetchNewsState = MutableStateFlow(
        Results<NewsDTO>(
            status = ResultStatus.INITIAL,
            data = null,
            message = null
        )
    )

    val getNewsState = MutableStateFlow(
        Results<List<Articles>>(
            status = ResultStatus.INITIAL,
            data = null,
            message = null
        )
    )


    init {
        fetchNews() // Ensure both calls are necessary
        getNews()
    }

    fun fetchNews() {
        viewModelScope.launch {
            fetchNewsState.emit(Results.loading())
            remoteRepository.getNews()
                .catch { e ->
                    fetchNewsState.emit(Results.error("Failed to fetch news: ${e.message}"))
                }
                .collect { results ->
                    results.data?.let {
                        databaseDataSource.saveNews(it.articles)
                    }
                }
        }
    }

    private fun getNews() {
        viewModelScope.launch {
            getNewsState.emit(Results.loading())
            databaseDataSource.getNews()
                .catch {
                    getNewsState.emit(Results.error("Something went wrong. Please try again later."))
                }
                .collect {
                    getNewsState.value = Results.success(it)
                }
        }
    }
}
