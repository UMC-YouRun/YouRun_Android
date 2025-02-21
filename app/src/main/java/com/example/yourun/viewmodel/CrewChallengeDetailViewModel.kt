package com.example.yourun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.CrewChallengeDetailRes
import com.example.yourun.model.data.response.CrewChallengeMateRes
import com.example.yourun.model.network.ApiResponse
import com.example.yourun.model.repository.ChallengeRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CrewChallengeDetailViewModel(private val repository: ChallengeRepository) : ViewModel() {

    private val _crewChallengeDetail = MutableLiveData<CrewChallengeDetailRes?>()
    val crewChallengeDetail: LiveData<CrewChallengeDetailRes?> get() = _crewChallengeDetail

    private val _challengeData = MutableLiveData<CrewChallengeMateRes?>() // ✅ 여기에 변수 추가!
    val challengeData: LiveData<CrewChallengeMateRes?> get() = _challengeData

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


    fun joinCrewChallenge(challengeId: Long) {
        viewModelScope.launch {
            try {
                val response: Response<ApiResponse<CrewChallengeMateRes>> =
                    repository.joinCrewChallenge(challengeId)

                if (response.isSuccessful && response.body()?.status == 200) {
                    _challengeData.postValue(response.body()?.data) // ✅ 챌린지 정보 저장
                    _joinSuccess.postValue(true) // ✅ 성공 여부 저장
                } else {
                    _joinSuccess.postValue(false) // ❌ 실패 처리
                }
            } catch (e: Exception) {
                _joinSuccess.postValue(false) // ❌ 네트워크 오류 처리
            }
        }
    }
}