package com.example.dailydigest.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailydigest.remoteRepository.RemoteRepository
import com.example.dailydigest.dto.NewsDTO
import domain.models.ResultStatus
import domain.models.Results
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val remoteRepository: RemoteRepository):
    ViewModel() {
    val newsState = MutableStateFlow(
        Results<NewsDTO>(
            status = ResultStatus.INITIAL,
            data = null,
            message = null
        )
    )

    init {
        fetchNews()
    }



    fun fetchNews() {
        viewModelScope.launch {
            newsState.value = Results.loading()
            remoteRepository.getNews().collect{ results ->
                newsState.value = results
            }
        }

    }
}