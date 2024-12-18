package com.example.dailydigest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import com.example.dailydigest.presentation.authentication.LoginPage
import com.example.dailydigest.ui.theme.DailyDigestTheme


@ExperimentalComposeApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyDigestTheme {
                Navigator(LoginPage())
            }
        }
    }
}
@Preview
@Composable
fun AppAndroidPreview() {
    DailyDigestTheme {}
}