package com.example.dailydigest.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.SubcomposeAsyncImage
import com.example.dailydigest.dto.Articles
import com.example.dailydigest.ui.theme.LightBlue
import com.example.dailydigest.ui.theme.Orange
import com.example.dailydigest.viewmodels.NewsViewModel
import domain.models.ResultStatus
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.Locale

data class NewsPage(val id: Long) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val newsViewModel: NewsViewModel = koinInject()
        val articleState = newsViewModel.getArticleState.collectAsState().value


        LaunchedEffect (id) {
            newsViewModel.getArticle(id)
        }
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    title = { Text("Details") },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = LightBlue
                    )
                )
            }
        ) { padding ->
            when(articleState.status){
                ResultStatus.INITIAL,
                ResultStatus.LOADING -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(35.dp))
                    }
                }

                ResultStatus.SUCCESS -> {
                    if (articleState.data == null){
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            Text("Article is not available at the moment")
                        }
                    }else {
                        NewsScreen(modifier = Modifier.padding(padding), articleState.data)
                    }
                }
                ResultStatus.ERROR -> {
                    Box(
                      modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text("Something is Wrong")
                    }
                }

                ResultStatus.UNAUTHENTICATED -> TODO()
            }

        }
    }
}

@Composable
fun NewsScreen(modifier: Modifier, news: Articles) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val parsedDate = inputFormat.parse(news.publishedAt)
    val formattedDate = parsedDate?.let { outputFormat.format(it) } ?: "Unknown Date"

    LazyColumn(modifier = modifier) {
        item {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .padding(8.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y
                        )
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale *= zoom
                                offset = if (scale <= 1f) Offset(0f, 0f) else {
                                    Offset(
                                        offset.x + pan.x * zoom,
                                        offset.y + pan.y * zoom
                                    )
                                }
                            }
                        },
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
                                    .size(40.dp)
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                tint = Color.LightGray,
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = news.author,
                                modifier = Modifier.size(80.dp)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp)) // Add space after image

                Text(text = news.title, fontWeight = FontWeight.SemiBold)
                HorizontalDivider(modifier = Modifier.padding(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp)) // Add space after image

            Text(
                text = news.description ?: "No Description Available",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp)) // Add space after image
            Row {
                Text(
                    text = "Author: ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                news.author?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp)) // Add space after image

            Row {
                Text(
                    text = "Published at: ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = formattedDate)
            }
            Spacer(modifier = Modifier.height(24.dp)) // Add space after image

            ClickableButton(newsUrl = news.url)
        }
    }
}

@Composable
fun ClickableButton(newsUrl: String) {
    val context = LocalContext.current
    Button(
        modifier = Modifier.height(50.dp),
        colors = ButtonColors(
            containerColor = Orange,
            contentColor = Color.Black,
            disabledContainerColor = Orange,
            disabledContentColor = Color.Black
        ),
        onClick = {
            // Open the URL in the browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsUrl))
            ContextCompat.startActivity(context, intent, null)
        },
    ) {
        Text(
            text = "More Information",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
@Composable
fun ArticleScrollView(articles: List<Articles>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(articles, key = { article -> article.id ?: 0 }) {
            NewsCard(news = it)
        }
    }
}





