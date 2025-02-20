package com.example.yourun.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourun.model.repository.ChallengeRepository

class CrewChallengeDetailViewModelFactory(private val repository: ChallengeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrewChallengeDetailViewModel::class.java)) {
            return CrewChallengeDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}