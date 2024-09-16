package com.example.dailydigest.di

import com.example.dailydigest.viewmodels.NewsViewModel
import com.example.dailydigest.remote.network.ApiHelper
import com.example.dailydigest.remoteRepository.RemoteRepository
import com.example.dailydigest.remoteRepository.RemoteRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

fun commonModule(): Module = module{
    single<ApiHelper> {
        ApiHelper()
    }

    single<RemoteRepository>{
        RemoteRepositoryImpl(apiHelper = get())
    }
    single<NewsViewModel>{
        NewsViewModel(remoteRepository = get())
    }
}