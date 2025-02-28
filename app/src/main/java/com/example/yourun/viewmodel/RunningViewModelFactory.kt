package com.example.yourun.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourun.model.repository.RunningRepository

class RunningViewModelFactory(private val repository: RunningRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunningViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RunningViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
