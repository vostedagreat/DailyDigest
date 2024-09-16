package com.example.dailydigest

import android.app.Application
import com.example.dailydigest.di.commonModule
import com.example.dailydigest.di.KoinInit
import org.koin.android.ext.koin.androidContext


class DailyDigest: Application() {
    override fun onCreate() {
        super.onCreate()
        KoinInit().init {
            androidContext(this@DailyDigest)
            modules(
                listOf(
                    commonModule()
                ),
            )
        }
    }
}