package com.example.yourun.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RunningViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunningViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RunningViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
