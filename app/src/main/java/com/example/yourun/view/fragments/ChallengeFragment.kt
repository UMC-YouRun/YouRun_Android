package com.example.yourun.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChallengeFragment : Fragment() {

    private lateinit var tvFacemakerName: TextView
    private lateinit var tvFacemakerDays: TextView
    private lateinit var tvFacemakerRole: TextView
    private lateinit var tvFacemakerTags: TextView
    private lateinit var tvTrailrunnerName: TextView
    private lateinit var tvTrailrunnerDays: TextView
    private lateinit var tvTrailrunnerRole: TextView
    private lateinit var tvTrailrunnerTags: TextView
    private lateinit var tvRunningMessage: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_running_challenge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvFacemakerName = view.findViewById(R.id.tv_facemaker_name)
        tvFacemakerDays = view.findViewById(R.id.tv_facemaker_days)
        tvFacemakerRole = view.findViewById(R.id.tv_facemaker_role)
        tvFacemakerTags = view.findViewById(R.id.tv_facemaker_tags)
        tvTrailrunnerName = view.findViewById(R.id.tv_trailrunner_name)
        tvTrailrunnerDays = view.findViewById(R.id.tv_trailrunner_days)
        tvTrailrunnerRole = view.findViewById(R.id.tv_trailrunner_role)
        tvTrailrunnerTags = view.findViewById(R.id.tv_trailrunner_tags)
        tvRunningMessage = view.findViewById(R.id.tv_running_message)

        fetchChallengeData()
    }

    private fun fetchChallengeData() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.getApiService().getChallengeData()

                if (response.isSuccessful) {
                    val challengeData = response.body()?.data

                    Log.d("ChallengeFragment", "API Response: $challengeData") // 🔥 로그 확인

                    withContext(Dispatchers.Main) {
                        if (challengeData != null) {
                            tvFacemakerName.text = challengeData.userNickName
                            tvFacemakerDays.text = "${challengeData.userCountDay}일째!"
                            tvFacemakerRole.text = challengeData.userTendency
                            tvFacemakerTags.text = challengeData.userHashTags.joinToString(", ")

                            tvTrailrunnerName.text = challengeData.challengeMateNickName
                            tvTrailrunnerDays.text = "${challengeData.challengeMateCountDay}일째!"
                            tvTrailrunnerRole.text = challengeData.challengeMateTendency
                            tvTrailrunnerTags.text = challengeData.challengeMateHashTags.joinToString(", ")

                            tvRunningMessage.text = "${challengeData.challengeDistance}km 달리기 도전!"
                        } else {
                            // ✅ `null` 데이터 처리
                            tvFacemakerName.text = "정보 없음"
                            tvFacemakerDays.text = "0일째!"
                            tvFacemakerRole.text = "정보 없음"
                            tvFacemakerTags.text = "정보 없음"

                            tvTrailrunnerName.text = "정보 없음"
                            tvTrailrunnerDays.text = "0일째!"
                            tvTrailrunnerRole.text = "정보 없음"
                            tvTrailrunnerTags.text = "정보 없음"

                            tvRunningMessage.text = "도전 정보 없음"

                            Log.e("ChallengeFragment", "서버에서 데이터가 null을 반환함.")
                        }
                    }
                } else {
                    Log.e("ChallengeFragment", "API Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ChallengeFragment", "Exception: ${e.message}")
            }
        }
    }


}


