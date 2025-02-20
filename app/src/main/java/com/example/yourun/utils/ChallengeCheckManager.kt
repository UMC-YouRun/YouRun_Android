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
    private var checkJob: Job? = null
    private val handler = Handler(Looper.getMainLooper())

    private val allowedActivities = setOf(
        "com.example.yourun.view.activities.MainActivity" // ✅ 메인 액티비티에서만 실행
    )

    fun init(context: Context) {
        prefs = context.getSharedPreferences("challenge_prefs", Context.MODE_PRIVATE)
        resetApiCallStatus() // ✅ 앱 실행 시 is_api_called 초기화 (앱 재시작 시 다시 실행되도록)
    }

    fun startPeriodicCheck(context: Context) {
        checkJob?.cancel() // 기존 실행 중인 잡 취소

        // ✅ 현재 실행 중인 액티비티 확인
        val activityName = getCurrentActivityName(context)

        // ✅ MainActivity에서만 실행
        if (activityName !in allowedActivities) {
            Log.d("ChallengeCheckManager", "현재 액티비티: $activityName → MainActivity에서만 실행됨")
            return
        }

        // ✅ 이미 실행된 적이 있으면 return (앱 실행 중에는 한 번만 실행)
        if (isApiAlreadyCalled()) {
            Log.d("ChallengeCheckManager", "API 체크 이미 완료됨 → 다시 실행 안함")
            return
        }

        checkJob = CoroutineScope(Dispatchers.IO).launch {
            delay(30000) // 30초 후 실행
            val result = checkChallengeMatching()
            result?.let { (isSoloMatching, isCrewMatching) ->
                if (isSoloMatching) {
                    Log.d("ChallengeCheckManager", "솔로 챌린지 매칭됨!")
                    openChallengeActivity(context, "SOLO")
                } else if (isCrewMatching) {
                    Log.d("ChallengeCheckManager", "크루 챌린지 매칭됨!")
                    openChallengeActivity(context, "CREW")
                }
            }
            setApiCalled() // ✅ API 호출 상태 저장 → 앱 실행 중에는 다시 실행되지 않도록
        }
    }

    /**
     * ✅ 앱을 처음 실행할 때 API 호출 상태 초기화
     */
    private fun resetApiCallStatus() {
        prefs.edit().putBoolean("is_api_called", false).apply()
    }

    private fun isApiAlreadyCalled(): Boolean {
        return prefs.getBoolean("is_api_called", false)
    }

    private fun setApiCalled() {
        prefs.edit().putBoolean("is_api_called", true).apply()
    }

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

    private fun getCurrentActivityName(context: Context): String? {
        return try {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            val runningTask = activityManager.appTasks.firstOrNull()?.taskInfo?.topActivity
            runningTask?.className
        } catch (e: Exception) {
            Log.e("ChallengeCheckManager", "현재 액티비티 가져오는 중 오류 발생", e)
            null
        }
    }
}
