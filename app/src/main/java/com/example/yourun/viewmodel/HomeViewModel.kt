package com.example.yourun.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.ChallengeData
import com.example.yourun.model.data.UserMateInfo
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _isPressedCrew = MutableLiveData(false)
    val isPressedCrew: LiveData<Boolean> get() = _isPressedCrew

    private val _isPressedSolo = MutableLiveData(false)
    val isPressedSolo: LiveData<Boolean> get() = _isPressedSolo

    private val _challengeData = MutableLiveData<ChallengeData?>()
    val challengeData: LiveData<ChallengeData?> get() = _challengeData

    private val _recommendMates = MutableLiveData<List<UserMateInfo>>()
    val recommendMates: LiveData<List<UserMateInfo>> get() = _recommendMates

    private val _likedMates = MutableLiveData<Set<Long>>(mutableSetOf())
    val likedMates: LiveData<Set<Long>> get() = _likedMates


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

    fun fetchRecommendMates() {
        viewModelScope.launch {
            try {
                val token = "Bearer " + ApiClient.TokenManager.getToken()
                Log.d("HomeViewModel", "전송할 토큰: $token")

                val response = repository.getRecommendMates(token)

                response?.data?.let { mateList ->  // response가 null이 아닐 때만 실행
                    if (mateList.isEmpty()) {
                        Log.e("HomeViewModel", "mateList : 추천 메이트 데이터 없음")
                        _recommendMates.value = emptyList()
                    } else {
                        _recommendMates.value = mateList.take(5) // 최대 5명만 표시
                    }
                    return@launch
                }

                // response 자체가 null이면 빈 리스트 할당
                Log.e("HomeViewModel", "response : 추천 메이트 데이터 없음")
                _recommendMates.value = emptyList()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "추천 메이트 가져오는 중 오류 발생", e)
            }
        }
    }

    fun addMate(mateId: Long) {
        viewModelScope.launch {
            try {
                val token = "Bearer " + ApiClient.TokenManager.getToken()
                val success = repository.addMate(token, mateId)

                if (success) {
                    _likedMates.value = _likedMates.value?.plus(mateId) ?: setOf(mateId)
                    Log.d("HomeViewModel", "메이트 추가 성공: $mateId")
                } else {
                    Log.e("HomeViewModel", "메이트 추가 실패: $mateId")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "좋아요 요청 중 오류 발생", e)
            }
        }
    }
}
