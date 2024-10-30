package com.example.dailydigest.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.dailydigest.viewmodels.NewsViewModel
import org.koin.compose.koinInject
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.example.dailydigest.modules.SearchBar
import com.example.dailydigest.viewmodels.SearchViewModel
import domain.models.ResultStatus


class SearchNewsPage : Screen {
    @Composable
    override fun Content() {
        SearchNewsScreen(modifier = Modifier)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchNewsScreen(modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.currentOrThrow
    val searchQuery = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val searchViewModel = koinInject<SearchViewModel>()
    val searchedArticles = searchViewModel.searchNewsState.collectAsState().value

    Scaffold(modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .onGloballyPositioned {
                            focusRequester.requestFocus()
                        }
                        .padding(4.dp)
                    ) {
                       SearchBar(
                           description = "Search articles",
                           value = searchQuery.value,
                           onValueChange = { newText ->
                               searchQuery.value = newText
                               searchViewModel.searchNews(newText)
                           },
                           modifier = Modifier
                               .focusRequester(focusRequester)
                               .fillMaxWidth(),
                           onExit = { navigator.pop() }
                       )
                    }
                },
            )
        }
    ) { padding ->

        when(searchedArticles.status){
            ResultStatus.INITIAL,
            ResultStatus.LOADING,
            ResultStatus.ERROR,
            ResultStatus.UNAUTHENTICATED -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "Type to search")
                }
            }
            ResultStatus.SUCCESS -> {
                if(searchedArticles.data.isNullOrEmpty()){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(text = "Article not found")
                    }
                }else{
                    NewsListView(
                       news = searchedArticles.data,
                        modifier = Modifier.padding(padding)
                    )
                }

            }
        }
    }

}

