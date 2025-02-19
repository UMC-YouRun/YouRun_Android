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

    private val excludedActivities = setOf(
        "com.example.yourun.view.activities.OnboardingActivity",
        "com.example.yourun.view.activities.SignUpActivity",
        "com.example.yourun.view.activities.LoginActivity"
    )

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
     * ✅ 특정 액티비티에서는 `startPeriodicCheck` 실행 방지
     */
    fun startPeriodicCheck(context: Context) {
        checkJob?.cancel() // 기존 실행 중인 잡이 있다면 취소

        // ✅ 현재 실행 중인 액티비티 확인
        val activityName = getCurrentActivityName(context)

        // 특정 액티비티라면 실행하지 않음
        if (activityName in excludedActivities) {
            Log.d("ChallengeCheckManager", "현재 액티비티 제외 목록에 있음: $activityName → 주기적 체크 실행 안함")
            return
        }

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

    /**
     * ✅ 현재 실행 중인 액티비티 이름 가져오기
     */
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
