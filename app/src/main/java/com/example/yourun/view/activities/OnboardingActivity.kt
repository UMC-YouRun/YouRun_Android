package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.yourun.R

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // 첫 번째 Lottie 애니메이션 설정
        val animationView1 = findViewById<LottieAnimationView>(R.id.lottieAnimationView1)
        animationView1.setAnimation("onboarding_yourun1.json") // Lottie 애니메이션 파일 경로
        animationView1.playAnimation()


        // 일정 시간이 지난 후 로그인 화면으로 이동
        Handler().postDelayed({
            // 로그인 화면으로 전환
            val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
            startActivity(intent)
            finish() // 온보딩 화면을 스택에서 제거
        }, 5000) // 5초 후에 로그인 화면으로 전환 (5000ms)
    }
}
