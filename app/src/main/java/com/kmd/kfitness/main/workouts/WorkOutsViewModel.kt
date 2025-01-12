package com.kmd.kfitness.main.workouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkOutsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Workouts Fragment"
    }
    val text: LiveData<String> = _text
}