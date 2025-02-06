package com.example.yourun.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.ChallengeData
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.HomeChallengeRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeChallengeRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _isPressedCrew = MutableLiveData(false)
    val isPressedCrew: LiveData<Boolean> get() = _isPressedCrew

    private val _isPressedSolo = MutableLiveData(false)
    val isPressedSolo: LiveData<Boolean> get() = _isPressedSolo

    private val _challengeData = MutableLiveData<ChallengeData?>()
    val challengeData: LiveData<ChallengeData?> get() = _challengeData

    fun toggleCrewButton() {
        _isPressedCrew.value = !(_isPressedCrew.value ?: false)
        _isPressedSolo.value = false // Solo 버튼 해제
    }

    fun toggleSoloButton() {
        _isPressedSolo.value = !(_isPressedSolo.value ?: false)
        _isPressedCrew.value = false // Crew 버튼 해제
    }

    fun fetchHomeChallengeData() {
        viewModelScope.launch {
            try {
                val token = "Bearer " + ApiClient.TokenManager.getToken()
                Log.d("HomeViewModel", "전송할 토큰: $token") // 토큰 로그 추가

                val response = repository.getHomeChallengeData(token)
                if (response == null) {
                    Log.e("HomeViewModel", "서버에서 유효한 챌린지 데이터를 반환하지 않았습니다.")
                    return@launch // 서버 응답이 null이면 UI 업데이트를 하지 않음
                }

                Log.d("HomeViewModel", "서버 응답 성공: $response")

                _challengeData.value = response // ChallengeData 업데이트
            } catch (e: Exception) {
                Log.e("HomeViewModel", "서버 요청 중 오류 발생", e)
            }
        }
    }
}
