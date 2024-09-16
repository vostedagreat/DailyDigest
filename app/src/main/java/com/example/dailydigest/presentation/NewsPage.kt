package com.example.dailydigest.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.SubcomposeAsyncImage
import com.example.dailydigest.dto.Articles
import com.example.dailydigest.ui.theme.LightBlue
import com.example.dailydigest.viewmodels.NewsViewModel
import org.koin.compose.koinInject


data class NewsPage(val article: Articles) : Screen {
    @Composable
    override fun Content() {
        NewsScreen(article)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen (news: Articles) {
    val navigator = LocalNavigator.currentOrThrow
    val newsViewModel = koinInject<NewsViewModel>()
    val newsState = newsViewModel.newsState.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(modifier = Modifier,
                        onClick = {navigator.pop() }){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                title = {
                    Text(
                        text = news.title,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = LightBlue ,

                    )
            )
        }
    ){ padding -> 4.dp
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            ){
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
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.End
            ) {

                news.author?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
            Text(text = news.description ?:"", style = MaterialTheme.typography.bodyLarge)
            ClickableLink(newsUrl = news.url)
            Text(Utils.Utils.convertTimestampToDate(news.publishedAt))
            Text(Utils.Utils.convertTimestampToDate(news.publishedAt))

        }
    }
}
@Composable
fun ClickableLink(newsUrl: String) {
    val context = LocalContext.current

    ClickableText(
        text = AnnotatedString(newsUrl),
        style = MaterialTheme.typography.bodySmall,
        onClick = {
            // Open the URL in the browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsUrl))
            ContextCompat.startActivity(context, intent, null)
        }
    )
}

