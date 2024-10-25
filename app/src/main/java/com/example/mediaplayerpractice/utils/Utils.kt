package com.example.mediaplayerpractice.utils

import android.content.Context
import org.koin.java.KoinJavaComponent.inject

object Utils {

    private val context: Context by inject(Context::class.java)

    fun readJsonFromAssets(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}