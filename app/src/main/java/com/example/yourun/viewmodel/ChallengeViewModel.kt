package com.example.yourun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.CrewChallengeRes
import com.example.yourun.model.data.SoloChallengeRes
import com.example.yourun.model.repository.ChallengeRepository
import kotlinx.coroutines.launch

class ChallengeViewModel(private val repository: ChallengeRepository) : ViewModel() {

    private val _pendingCrewChallenges = MutableLiveData<List<CrewChallengeRes>?>()
    val pendingCrewChallenges: LiveData<List<CrewChallengeRes>?> get() = _pendingCrewChallenges

    fun fetchPendingCrewChallenges() {
        viewModelScope.launch {
            val challenges = repository.getPendingCrewChallenges()
            if (challenges.isNullOrEmpty()) {
                _pendingCrewChallenges.postValue(emptyList()) // 🔥 빈 리스트라도 post
                println("VIEWMODEL_DEBUG: 크루 챌린지 데이터 없음")
            } else {
                println("VIEWMODEL_DEBUG: 받은 크루 챌린지 리스트 = $challenges")
                _pendingCrewChallenges.postValue(challenges)
            }
        }
    }

    private val _pendingPersonalChallenges = MutableLiveData<List<SoloChallengeRes>?>()
    val pendingPersonalChallenges: LiveData<List<SoloChallengeRes>?> get() = _pendingPersonalChallenges

    fun fetchPendingPersonalChallenges() {
        viewModelScope.launch {
            val challenges = repository.getPendingPersonalChallenges()
            if (challenges.isNullOrEmpty()) {
                _pendingPersonalChallenges.postValue(emptyList()) // 🔥 빈 리스트라도 post
                println("VIEWMODEL_DEBUG: 솔로 챌린지 데이터 없음")
            } else {
                println("VIEWMODEL_DEBUG: 받은 솔로 챌린지 리스트 = $challenges")
                _pendingPersonalChallenges.postValue(challenges)
            }
        }
    }

}