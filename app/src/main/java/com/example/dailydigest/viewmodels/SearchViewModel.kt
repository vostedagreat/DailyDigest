package com.example.dailydigest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailydigest.dto.Articles
import com.example.dailydigest.local.Database.DatabaseDataSource
import domain.models.ResultStatus
import domain.models.Results
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class SearchViewModel (private val databaseDataSource: DatabaseDataSource) : ViewModel() {
    val searchNewsState = MutableStateFlow(
        Results<List<Articles>>(
            status = ResultStatus.INITIAL,
            data = null,
            message = null
        )
    )

    fun searchNews(searchTerm: String) {
        viewModelScope.launch {
            val articles = databaseDataSource.searchNews(searchTerm)
            searchNewsState.value = Results.success(articles)
        }
    }
}