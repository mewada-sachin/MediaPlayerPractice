package com.example.mediaplayerpractice.di

import com.example.mediaplayerpractice.ui.main.MainActivityViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object DiUtils {

    val appModule = module {
        viewModel { MainActivityViewModel() }
    }
}
