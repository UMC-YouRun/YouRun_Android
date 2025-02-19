package com.example.yourun.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.response.CrewChallengeResultData
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CrewChallengeResultViewModel : ViewModel() {

    private val _challengeData = MutableStateFlow(CrewChallengeResultData())
    val challengeData: StateFlow<CrewChallengeResultData> = _challengeData

    fun fetchCrewChallengeResult() {
        viewModelScope.launch {
            try {
                Log.d("CrewChallengeViewModel", "📢 API 요청 시작")
                val response = ApiClient.getChallengeApiService().getCrewChallengeResult()

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("CrewChallengeViewModel", "✅ API 응답 성공: $body")
                    body?.data?.let { data ->
                        _challengeData.value = data
                    } ?: Log.e("CrewChallengeViewModel", "⚠️ 응답 데이터가 null")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("CrewChallengeViewModel", "❌ API 응답 실패 - 코드: ${response.code()}, 메시지: ${response.message()}")
                    Log.e("CrewChallengeViewModel", "❗ 서버 에러 응답 본문: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("CrewChallengeViewModel", "❗ 예외 발생: ${e.message}")
            }
        }
    }
}
