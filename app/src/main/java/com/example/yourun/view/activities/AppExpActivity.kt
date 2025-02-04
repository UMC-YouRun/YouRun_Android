package com.example.yourun.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import com.example.yourun.view.fragments.AppExpFragment

class AppExpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_exp)

        // 이전 화면에서 받은 데이터
        val receivedData = intent.getStringExtra("nickname")

        // Fragment에 데이터 전달
        val fragment = AppExpFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", receivedData)
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.app_exp_fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}