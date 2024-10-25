package com.example.mediaplayerpractice

import android.app.Application
import com.example.mediaplayerpractice.di.DiUtils
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MediaPlayerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@MediaPlayerApplication)
            modules(DiUtils.appModule)  }
    }
}
