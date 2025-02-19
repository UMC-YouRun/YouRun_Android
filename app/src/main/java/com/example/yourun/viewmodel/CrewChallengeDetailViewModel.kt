package com.example.yourun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.CrewChallengeDetailRes
import com.example.yourun.model.repository.ChallengeRepository
import kotlinx.coroutines.launch

class CrewChallengeDetailViewModel(private val repository: ChallengeRepository) : ViewModel() {

    private val _crewChallengeDetail = MutableLiveData<CrewChallengeDetailRes?>()
    val crewChallengeDetail: LiveData<CrewChallengeDetailRes?> get() = _crewChallengeDetail

    fun fetchCrewChallengeDetail(challengeId: String) {
        viewModelScope.launch {
            val response = repository.getCrewChallengeDetail(challengeId.toString()) // API 호출
            if (response.isSuccessful) {
                _crewChallengeDetail.postValue(response.body())
            } else {
                _crewChallengeDetail.postValue(null)
            }
        }
    }
}