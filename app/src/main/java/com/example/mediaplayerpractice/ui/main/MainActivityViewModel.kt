package com.example.mediaplayerpractice.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerpractice.data.CategoryList
import com.example.mediaplayerpractice.data.MoviesCategory
import com.example.mediaplayerpractice.repository.NetworkRepository
import com.example.mediaplayerpractice.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch


class MainActivityViewModel(private val networkRepository: NetworkRepository) : ViewModel() {

    private val _liveData = MutableLiveData<List<MoviesCategory>>()
    val liveData: LiveData<List<MoviesCategory>> get() = _liveData

    fun getMoviesList(fileName: String) {
        viewModelScope.launch {
            Utils.readJsonFromAssets("$fileName.json")
            val list=
                Gson().fromJson<CategoryList>(
                    Utils.readJsonFromAssets("$fileName.json"),
                    object : TypeToken<CategoryList>() {}.type
                ).category
            _liveData.postValue(list)
        }
    }
}