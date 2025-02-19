package com.example.yourun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.SoloChallengeDetailRes
import com.example.yourun.model.repository.ChallengeRepository
import kotlinx.coroutines.launch

class SoloChallengeDetailViewModel(private val repository: ChallengeRepository) : ViewModel() {

    private val _soloChallengeDetail = MutableLiveData<SoloChallengeDetailRes?>()
    val soloChallengeDetail: LiveData<SoloChallengeDetailRes?> get() = _soloChallengeDetail

    fun fetchSoloChallengeDetail(challengeId: Long) {
        viewModelScope.launch {
            val response = repository.getSoloChallengeDetail(challengeId.toString())
            if (response.isSuccessful) {
                _soloChallengeDetail.postValue(response.body())
            } else {
                _soloChallengeDetail.postValue(null)
            }
        }
    }
}