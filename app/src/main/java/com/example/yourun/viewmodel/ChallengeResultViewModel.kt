package com.example.yourun.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.data.response.ChallengeResultResponse
import android.util.Log

class ChallengeResultViewModel : ViewModel() {

    private val _challengeData = MutableStateFlow<ChallengeResultResponse?>(null)
    val challengeData: StateFlow<ChallengeResultResponse?> get() = _challengeData

    fun fetchSoloChallengeResultData() {
        viewModelScope.launch {
            try {
                Log.d("ChallengeResultViewModel", "API 요청 시작")
                val response = ApiClient.getChallengeApiService().getSoloChallengeResultData()

                if (response.isSuccessful) {
                    val challengeResponse = response.body()?.data
                    _challengeData.value = challengeResponse
                    Log.d("ChallengeResultViewModel", "API 응답 성공: $challengeResponse")
                } else {
                    Log.e("ChallengeResultViewModel", "API Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ChallengeResultViewModel", "Exception: ${e.message}")
            }
        }
    }
}
