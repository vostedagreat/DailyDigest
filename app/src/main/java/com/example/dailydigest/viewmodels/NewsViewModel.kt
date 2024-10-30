package com.example.dailydigest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailydigest.dto.Articles
import com.example.dailydigest.dto.NewsDTO
import com.example.dailydigest.local.Database.DatabaseDataSource
import com.example.dailydigest.remoteRepository.RemoteRepository
import domain.models.ResultStatus
import domain.models.Results
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsViewModel(
    private val remoteRepository: RemoteRepository,
    private val databaseDataSource: DatabaseDataSource
) : ViewModel() {


    //val searchedArticles: List<Articles> = repository.getArticles().ifEmpty { emptyList() }
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()


    

    val newsViewFlow: StateFlow<Int?> = flowOf(null).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

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
   val getArticleState = MutableStateFlow(
       Results<Articles>(
           status = ResultStatus.INITIAL,
           data = null,
           message = null
       )
   )

    init {
        fetchNews() // Ensure both calls are necessary
        getNews()
    }

    fun getArticle(id: Long){
        viewModelScope.launch {
            getArticleState.emit(Results.loading())
            databaseDataSource.getArticle(id = id)
                .catch {
                    it.printStackTrace()
                    getArticleState.emit(Results.error("Please try again"))
                }
                .collect{
                    getArticleState.value = Results.success(it)
                }
        }
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

