package com.fetch.test

import android.app.Application
import com.fetch.test.core.data.api.ApiService
import com.fetch.test.core.data.api.createHttpClient
import com.fetch.test.core.data.database.AppDatabase
import com.fetch.test.core.data.preferences.AppPreferences
import com.fetch.test.core.data.repository.ListItemRepositoryImpl
import com.fetch.test.domain.repository.ListItemRepository
import com.fetch.test.domain.usecase.GetListItemsUseCase
import com.fetch.test.feature.list.ui.viewmodel.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin for dependency injection
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}

// Koin module for dependency injection
val appModule = module {
    // Single instance of Ktor HttpClient
    single { createHttpClient() }

    // Single instance of FetchApiService using the HttpClient
    single { ApiService(get()) }

    // Single instance of Room Database
    single { AppDatabase.getDatabase(androidContext()) }

    // Single instance of ListItemDao
    single { get<AppDatabase>().itemDao() }

    // Single instance of AppPreferences
    single { AppPreferences(androidContext()) }

    // Single instance of ItemRepository implementation
    single<ListItemRepository> { ListItemRepositoryImpl(get(), get()) }

    // Single instance of GetItemsUseCase
    single { GetListItemsUseCase(get()) }

    // ViewModel definition, injecting the GetItemsUseCase
    viewModel { HomeViewModel(get(), get()) }
}
