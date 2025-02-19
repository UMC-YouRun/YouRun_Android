package com.example.yourun.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.data.response.ResultContributionResponse
import android.util.Log

class ResultContributionViewModel : ViewModel() {

    private val _contributionData = MutableStateFlow<List<ResultContributionResponse>>(emptyList())
    val contributionData: StateFlow<List<ResultContributionResponse>> get() = _contributionData

    fun fetchResultContributionData() {
        viewModelScope.launch {
            try {
                Log.d("ResultContributionViewModel", "API 요청 시작")
                val response = ApiClient.getChallengeApiService().getResultContribution()

                if (response.isSuccessful) {
                    val contributionResponse = response.body() ?: emptyList()
                    _contributionData.value = contributionResponse
                    Log.d("ResultContributionViewModel", "API 응답 성공: $contributionResponse")
                } else {
                    Log.e("ResultContributionViewModel", "API Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ResultContributionViewModel", "Exception: ${e.message}")
            }
        }
    }
}
