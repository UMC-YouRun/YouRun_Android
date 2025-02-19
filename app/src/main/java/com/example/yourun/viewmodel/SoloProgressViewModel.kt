package com.example.yourun.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.response.SoloChallengeProgressResponse
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SoloProgressViewModel : ViewModel() {

    private val _challengeProgress = MutableStateFlow<SoloChallengeProgressResponse?>(null)
    val challengeProgress: StateFlow<SoloChallengeProgressResponse?> = _challengeProgress

    fun fetchSoloChallengeProgress() {
        viewModelScope.launch {
            try {
                Log.d("SoloProgressViewModel", "📢 솔로 챌린지 진행도 API 요청 시작")
                val response = ApiClient.getChallengeApiService().getSoloChallengeProgress()

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("SoloProgressViewModel", "✅ API 응답 성공: $body")
                    body?.data?.let { data ->
                        _challengeProgress.value = data
                    } ?: Log.e("SoloProgressViewModel", "⚠️ 응답 데이터가 null")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SoloProgressViewModel", "❌ API 응답 실패 - 코드: ${response.code()}, 메시지: ${response.message()}")
                    Log.e("SoloProgressViewModel", "❗ 서버 에러 응답 본문: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("SoloProgressViewModel", "❗ 예외 발생: ${e.message}")
            }
        }
    }
}
