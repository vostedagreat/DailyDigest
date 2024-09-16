package com.example.dailydigest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.SubcomposeAsyncImage
import com.example.dailydigest.dto.Articles
import com.example.dailydigest.ui.theme.DailyDigestTheme
import com.example.dailydigest.ui.theme.LightBlue
import com.example.dailydigest.viewmodels.NewsViewModel
import cafe.adriel.voyager.navigator.currentOrThrow
import domain.models.ResultStatus
import org.koin.compose.koinInject


@ExperimentalComposeApi
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyDigestTheme {
                val newsViewModel = koinInject<NewsViewModel>()
                val newsState = newsViewModel.newsState.collectAsState().value
                LaunchedEffect(true) {
                    newsViewModel.fetchNews()
                }
                Scaffold(
                    topBar = {

                        CenterAlignedTopAppBar(
                            title = { Text("Daily Digest") },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = LightBlue ,

                            )
                        )
                    },
                ) { padding ->

                        when (newsState.status) {
                            ResultStatus.LOADING,
                            ResultStatus.INITIAL -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(25.dp))
                                }
                            }

                            ResultStatus.SUCCESS -> {
                                if (newsState.data?.articles.isNullOrEmpty()) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "No news at the moment")
                                    }
                                } else {
                                    newsState.data?.let {
                                        NewsListView(
                                            modifier = Modifier.padding(
                                                padding
                                            ), news = it.articles
                                        )
                                    }
                                }
                            }

                            ResultStatus.ERROR -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = newsState.message ?: "Something went wrong")
                                }
                            }
                        }
                }
            }
        }
    }
}

@Composable
fun NewsListView(news: List<Articles>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 8.dp, 4.dp)
            .fillMaxSize()
    ) {
        items(news) {
            NewsCard(news = it)
        }
    }
}

@Composable
fun NewsCard(news: Articles) {
    val newsViewModel: NewsViewModel = koinInject()
    val navigator = LocalNavigator.currentOrThrow



    Card(

            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(8.dp)

                .clickable {
                    navigator.push(NewsPage(news))
                }

        ) {
            Row() {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(125.dp)
                        .padding(end = 10.dp),
                    contentAlignment = Alignment.Center
                ) {

                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
                            .background(Color.LightGray),
                        model = news.urlToImage,
                        contentScale = ContentScale.Fit,
                        contentDescription = news.urlToImage,
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                            }
                        },
                        error = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.LightGray)
                                    .align(Alignment.Center),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = news.author,
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    )

                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = news.author ?: "N/A",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = news.title,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }


    }

