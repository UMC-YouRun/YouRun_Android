package com.example.yourun.view.activities

import android.app.Activity
import android.os.Bundle
import com.example.yourun.R
import android.content.Intent
import android.os.Handler
import android.os.Looper

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        // 2초 딜레이 후 로그인 화면으로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            // val intent = Intent(this, AppExpActivity::class.java)
            startActivity(intent)
            finish() // 온보딩 화면을 종료하여 뒤로가기 시 돌아오지 않도록 설정
        }, 2000)
    }
}