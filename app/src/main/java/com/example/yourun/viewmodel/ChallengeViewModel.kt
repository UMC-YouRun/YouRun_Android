/*package com.example.yourun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.CrewChallengeRes
import com.example.yourun.model.data.MemberTendencyInfo
import com.example.yourun.model.data.SoloChallengeRes
import com.example.yourun.model.data.Tendency
import com.example.yourun.model.repository.ChallengeRepository
import kotlinx.coroutines.launch

class ChallengeViewModel(private val repository: ChallengeRepository) : ViewModel() {

    private val _pendingCrewChallenges = MutableLiveData<List<CrewChallengeRes>?>()
    val pendingCrewChallenges: LiveData<List<CrewChallengeRes>?> get() = _pendingCrewChallenges

    fun fetchPendingCrewChallenges() {
        viewModelScope.launch {
            /*
            val challenges = repository.getPendingCrewChallenges()
            if (challenges.isNullOrEmpty()) {
                _pendingCrewChallenges.postValue(emptyList()) // 🔥 빈 리스트라도 post
                println("VIEWMODEL_DEBUG: 크루 챌린지 데이터 없음")
            } else {
                println("VIEWMODEL_DEBUG: 받은 크루 챌린지 리스트 = $challenges")
                _pendingCrewChallenges.postValue(challenges)
            }
            */

            val challenges = repository.getPendingCrewChallenges()

            if (challenges.isNullOrEmpty()) {
                // 🔥 리스트가 비어 있으면 더미 데이터 추가
                val dummyChallenges = listOf(
                    CrewChallengeRes(
                        challengeId = 100,
                        crewName = "테스터",
                        challengePeriod = 5,
                        remaining = 2,
                        reward = 2,
                        participantIdsInfo = listOf(
                            MemberTendencyInfo(userId = 1, memberTendencyRaw = "스프린터"),
                            MemberTendencyInfo(userId = 2, memberTendencyRaw = "페이스메이커")
                        )
                    ),
                )
                _pendingCrewChallenges.postValue(dummyChallenges)
                println("VIEWMODEL_DEBUG: API 응답 없음, 더미데이터 사용")
            } else {
                _pendingCrewChallenges.postValue(challenges)
                println("VIEWMODEL_DEBUG: 받은 솔로 챌린지 리스트 = $challenges")
            }

        }
    }

    private val _pendingPersonalChallenges = MutableLiveData<List<SoloChallengeRes>?>()
    val pendingPersonalChallenges: LiveData<List<SoloChallengeRes>?> get() = _pendingPersonalChallenges

    fun fetchPendingPersonalChallenges() {
        viewModelScope.launch {
            /*
            val challenges = repository.getPendingPersonalChallenges()
            if (challenges.isNullOrEmpty()) {
                _pendingPersonalChallenges.postValue(emptyList()) // 🔥 빈 리스트라도 post
                println("VIEWMODEL_DEBUG: 솔로 챌린지 데이터 없음")
            } else {
                println("VIEWMODEL_DEBUG: 받은 솔로 챌린지 리스트 = $challenges")
                _pendingPersonalChallenges.postValue(challenges)
            }
             */

            val challenges = repository.getPendingPersonalChallenges()

            if (challenges.isNullOrEmpty()) {
                // 🔥 리스트가 비어 있으면 더미 데이터 추가
                val dummyChallenges = listOf(
                    SoloChallengeRes(
                        challengeId = 100,
                        challengeDistance = 3,
                        challengePeriod = 5,
                        challengeCreatorNickname = "테스트 유저",
                        challengeCreatorHashtags = listOf("느긋하게", "음악과"),
                        reward = 2,
                        challengeCreatorTendency = Tendency.SPRINTER
                    ),
                    SoloChallengeRes(
                        challengeId = 101,
                        challengeDistance = 5,
                        challengePeriod = 7,
                        challengeCreatorNickname = "테스터",
                        challengeCreatorHashtags = listOf("에너자이저", "열정적"),
                        reward = 3,
                        challengeCreatorTendency = Tendency.PACEMAKER
                    )
                )
                _pendingPersonalChallenges.postValue(dummyChallenges)
                println("VIEWMODEL_DEBUG: API 응답 없음, 더미데이터 사용")
            } else {
                _pendingPersonalChallenges.postValue(challenges)
                println("VIEWMODEL_DEBUG: 받은 솔로 챌린지 리스트 = $challenges")
            }
        }
    }

}*/