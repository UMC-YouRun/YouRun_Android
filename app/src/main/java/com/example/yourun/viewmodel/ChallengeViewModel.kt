package com.example.yourun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.CrewChallengeRes
import com.example.yourun.model.repository.ChallengeRepository
import kotlinx.coroutines.launch

class ChallengeViewModel(private val repository: ChallengeRepository) : ViewModel() {

    private val _pendingCrewChallenges = MutableLiveData<List<CrewChallengeRes>?>()
    val pendingCrewChallenges: LiveData<List<CrewChallengeRes>?> get() = _pendingCrewChallenges

    fun fetchPendingCrewChallenges() {
        viewModelScope.launch {
            val challenges = repository.getPendingCrewChallenges()
            _pendingCrewChallenges.postValue(challenges)
        }
    }
}