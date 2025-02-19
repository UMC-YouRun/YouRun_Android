package com.example.yourun.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.response.CrewChallengeProgressResponse
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CrewProgressViewModel : ViewModel() {

    private val _crewProgressData = MutableStateFlow<CrewChallengeProgressResponse?>(null)
    val crewProgressData: StateFlow<CrewChallengeProgressResponse?> = _crewProgressData

    fun fetchCrewChallengeProgress() {
        viewModelScope.launch {
            try {
                val response = ApiClient.getChallengeApiService().getCrewChallengeProgress()
                if (response.isSuccessful) {
                    _crewProgressData.value = response.body()?.data
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
