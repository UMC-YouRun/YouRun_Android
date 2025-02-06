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
                Log.d("HomeViewModel", "홈 챌린지 데이터 요청 시작")

                val response = repository.getHomeChallengeData() // 토큰을 넘기지 않음 (Interceptor 처리)

                if (response == null) {
                    Log.e("HomeViewModel", "서버에서 유효한 챌린지 데이터를 반환하지 않았습니다.")
                    _challengeData.value = null // UI가 반응할 수 있도록 명확히 null 설정
                    return@launch
                }

                Log.d("HomeViewModel", "서버 응답 성공: $response")
                _challengeData.value = response // ChallengeData 업데이트

            } catch (e: Exception) {
                Log.e("HomeViewModel", "서버 요청 중 오류 발생", e)
                _challengeData.value = null // 오류 발생 시에도 UI가 반응할 수 있도록 null 설정
            }
        }
    }


    fun fetchRecommendMates() {
        viewModelScope.launch {
            try {
                Log.d("HomeViewModel", "추천 메이트 요청 시작")

                val response = repository.getRecommendMates() // 토큰을 넘기지 않음 (Interceptor 처리)

                if (response == null) {
                    Log.e("HomeViewModel", "response가 null입니다. 빈 리스트 반환")
                    _recommendMates.value = emptyList()
                    return@launch
                }

                response.data.let { mateList ->
                    _recommendMates.value = if (mateList.isEmpty()) {
                        Log.e("HomeViewModel", "추천 메이트 데이터 없음")
                        emptyList()
                    } else {
                        Log.d("HomeViewModel", "추천 메이트 목록 가져오기 성공: ${mateList.size}명")
                        mateList.take(5) // 최대 5명만 표시
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "추천 메이트 가져오는 중 오류 발생", e)
                _recommendMates.value = emptyList() // 네트워크 오류 발생 시 빈 리스트 할당
            }
        }
    }

    fun addMate(mateId: Long) {
        viewModelScope.launch {
            try {
                Log.d("HomeViewModel", "메이트 추가 요청 시작 (mateId: $mateId)")

                val success = repository.addMate(mateId) // 토큰을 넘기지 않음 (Interceptor 처리)

                if (success) {
                    _likedMates.value = (_likedMates.value ?: emptySet()) + mateId // Set 업데이트 개선
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
