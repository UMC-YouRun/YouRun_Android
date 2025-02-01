package com.example.yourun.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.HomeChallengeResponse
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

    private val _challengeData = MutableLiveData<HomeChallengeResponse>()
    val challengeData: LiveData<HomeChallengeResponse> get() = _challengeData

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
                // Application Context를 사용하여 SharedPreferences 접근
                val token = "Bearer " + (ApiClient.getAccessTokenFromSharedPreferences(getApplication()) ?: "")
                _challengeData.value = repository.getHomeChallengeData(token)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching home challenge data", e)
            }
        }
    }

}
