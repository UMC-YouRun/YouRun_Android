/*package com.example.yourun.view.activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.yourun.R
import com.example.yourun.model.network.ApiClient
import com.example.yourun.viewmodel.ChallengeViewModel
import com.example.yourun.viewmodel.CrewChallengeDetailViewModel


class CrewChallengeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crew_challenge_detail)

        val challengeId = intent.getLongExtra("CHALLENGE_ID", -1L)

        if (challengeId == -1L) {
            Log.e("INTENT_ERROR", "CHALLENGE_ID가 전달되지 않음")
            finish() // 필수 데이터가 없으므로 액티비티 종료
            return
        }

        Log.d("INTENT_SUCCESS", "받은 CHALLENGE_ID: $challengeId")

        fetchChallengeDetail(challengeId)
    }

    private fun fetchChallengeDetail(challengeId: Long) {
        Log.d("API_CALL", "챌린지 상세조회 API 호출: challengeId=$challengeId")

        /*
        // 예제: API 요청 (Retrofit 사용 가정)
        ApiClient.getApiService().getChallengeDetail(challengeId)
            .enqueue(object : Callback<ChallengeDetailResponse> {
                override fun onResponse(
                    call: Call<ChallengeDetailResponse>,
                    response: Response<ChallengeDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("API_RESPONSE", "응답 성공: ${response.body()}")
                        updateUI(response.body()) // ✅ UI 업데이트 함수 호출
                    } else {
                        Log.e("API_ERROR", "응답 실패: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ChallengeDetailResponse>, t: Throwable) {
                    Log.e("API_ERROR", "API 호출 실패", t)
                }
            })
        */
    }
}*/