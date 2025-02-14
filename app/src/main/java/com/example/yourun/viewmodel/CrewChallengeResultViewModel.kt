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
                val response = ApiClient.getChallengeApiService().getCrewChallengeResult()
                if (response.isSuccessful) {
                    response.body()?.data?.let { data ->
                        _challengeData.value = data
                    }
                } else {
                    Log.e("CrewChallengeViewModel", "API Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CrewChallengeViewModel", "Exception: ${e.message}")
            }
        }
    }
}
