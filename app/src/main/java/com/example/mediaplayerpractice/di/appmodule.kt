package com.example.mediaplayerpractice.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.mediaplayerpractice.remote.ApiService
import com.example.mediaplayerpractice.repository.NetworkRepository
import com.example.mediaplayerpractice.repository.NetworkRepositoryImpl
import com.example.mediaplayerpractice.ui.main.MainActivityViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DiUtils {

    val appModule = module {

        single { createRetrofit(get()).create(ApiService::class.java) }
        single<NetworkRepository> { NetworkRepositoryImpl(get()) }
        viewModel { MainActivityViewModel(get()) }
    }

    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun createHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(ChuckerInterceptor(context))
            .build()
    }

    private fun createRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(createHttpClient(context))
            .build()
    }
}
