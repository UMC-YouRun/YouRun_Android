package com.example.yourun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.CrewChallengeDetailRes
import com.example.yourun.model.network.ApiResponse
import com.example.yourun.model.repository.ChallengeRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CrewChallengeDetailViewModel(private val repository: ChallengeRepository) : ViewModel() {

    private val _crewChallengeDetail = MutableLiveData<CrewChallengeDetailRes?>()
    val crewChallengeDetail: LiveData<CrewChallengeDetailRes?> get() = _crewChallengeDetail

    fun fetchCrewChallengeDetail(challengeId: String) {
        viewModelScope.launch {
            val response = repository.getCrewChallengeDetail(challengeId) // API 호출
            if (response.isSuccessful) {
                val responseBody = response.body()
                _crewChallengeDetail.postValue(responseBody?.data) // ✅ data 필드만 저장
            } else {
                _crewChallengeDetail.postValue(null)
            }
        }
    }

    private val _joinSuccess = MutableLiveData<Boolean>()
    val joinSuccess: LiveData<Boolean> get() = _joinSuccess

    fun joinCrewChallenge(challengeId: Long, participantIds: List<Long>) {
        viewModelScope.launch {
            try {
                val response = repository.joinCrewChallenge(challengeId, participantIds)

                if (response.isSuccessful && response.body()?.status == 200) {
                    _joinSuccess.postValue(true)
                } else {
                    _joinSuccess.postValue(false)
                }
            } catch (e: Exception) {
                _joinSuccess.postValue(false)
            }
        }
    }
}