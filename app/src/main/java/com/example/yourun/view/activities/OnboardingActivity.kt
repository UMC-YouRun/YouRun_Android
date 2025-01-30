package com.example.yourun.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.yourun.R

class OnboardingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // 2초 딜레이 후 로그인 화면으로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // 온보딩 화면을 종료하여 뒤로가기 시 돌아오지 않도록 설정
        }, 2000)
    }

    private fun finishOnboarding() {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isOnboardingCompleted", true).apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // 온보딩 종료
    }
}