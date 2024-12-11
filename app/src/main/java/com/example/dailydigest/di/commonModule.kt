package com.example.dailydigest.di

import com.example.dailydigest.AppDatabase
import com.example.dailydigest.local.Database.DatabaseDataSource
import com.example.dailydigest.local.Database.DatabaseDataSourceImpl
import com.example.dailydigest.local.Database.DatabaseDriverFactory
import com.example.dailydigest.viewmodels.NewsViewModel
import com.example.dailydigest.remote.network.ApiHelper
import com.example.dailydigest.remoteRepository.RemoteRepository
import com.example.dailydigest.remoteRepository.RemoteRepositoryImpl
import com.example.dailydigest.viewmodels.AuthenticationViewModel
import com.example.dailydigest.viewmodels.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun commonModule(): Module = module {
    single<ApiHelper> {
        ApiHelper()
    }
    single<RemoteRepository> {
        RemoteRepositoryImpl(apiHelper = get())
    }
    single<NewsViewModel> {
        NewsViewModel(remoteRepository = get(), databaseDataSource = get())
    }
    single<DatabaseDataSource> {
        DatabaseDataSourceImpl(appDatabase = get())
    }

    singleOf(::SearchViewModel)
    single<AppDatabase> {
        AppDatabase(driver = DatabaseDriverFactory(context = get()).create())
    }

    viewModelOf(::AuthenticationViewModel)

}





