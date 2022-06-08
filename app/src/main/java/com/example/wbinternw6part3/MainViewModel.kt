package com.example.wbinternw6part3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _data = MutableLiveData<String>()

    val liveData: LiveData<String> = _data

    fun applyNewState(string: String) {
        _data.value = string
    }
}