package com.example.yourun.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.*

object ChallengeCheckManager {

    private lateinit var prefs: SharedPreferences
    private val challengeApiService by lazy { ApiClient.getChallengeApiService() }
    private var checkJob: Job? = null // 코루틴 잡을 저장
    private val handler = Handler(Looper.getMainLooper()) // UI 스레드에서 실행

    fun init(context: Context) {
        prefs = context.getSharedPreferences("challenge_prefs", Context.MODE_PRIVATE)
    }

    fun isApiAlreadyCalled(): Boolean {
        return prefs.getBoolean("is_api_called", false)
    }

    private fun setApiCalled() {
        prefs.edit().putBoolean("is_api_called", true).apply()
    }

    /**
     * ✅ 앱 실행 후 30초 대기 후 주기적으로 챌린지 상태 확인
     */
    fun startPeriodicCheck(context: Context) {
        checkJob?.cancel() // 기존 실행 중인 잡이 있다면 취소

        checkJob = CoroutineScope(Dispatchers.IO).launch {
            delay(30000) //30초후
            while (true) {
                val result = checkChallengeMatching()
                result?.let { (isSoloMatching, isCrewMatching) ->
                    if (isSoloMatching) {
                        Log.d("ChallengeCheckManager", "솔로 챌린지 매칭됨!")
                        openChallengeActivity(context, "SOLO")
                        cancel() // 매칭되었으면 주기적 호출 중단
                    } else if (isCrewMatching) {
                        Log.d("ChallengeCheckManager", "크루 챌린지 매칭됨!")
                        openChallengeActivity(context, "CREW")
                        cancel() // 매칭되었으면 주기적 호출 중단
                    }
                }
                delay(60000) // ✅ 60초(1분)마다 확인
            }
        }
    }

    /**
     * ✅ API 요청 (호출 후 `Pair<Boolean, Boolean>` 반환)
     */
    private suspend fun checkChallengeMatching(): Pair<Boolean, Boolean>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = challengeApiService.checkMatching()
                if (response.isSuccessful) {
                    response.body()?.data?.let { data ->
                        return@withContext Pair(data.isSoloChallengeMatching, data.isCrewChallengeMatching)
                    }
                }
                Log.e("ChallengeCheckManager", "API 호출 실패: ${response.errorBody()?.string()}")
                null
            } catch (e: Exception) {
                Log.e("ChallengeCheckManager", "API 요청 중 오류 발생", e)
                null
            }
        }
    }


    private fun openChallengeActivity(context: Context, type: String) {
        handler.post {
            val intent = when (type) {
                "SOLO" -> android.content.Intent(context, com.example.yourun.view.activities.ChallengeStartActivity::class.java)
                "CREW" -> android.content.Intent(context, com.example.yourun.view.activities.CrewChallengeStartActivity::class.java)
                else -> return@post
            }
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}
