package com.example.yourun.view.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R

class RunningChallengeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_challenge)

        val topBarTitle: TextView = findViewById(R.id.txt_top_bar_with_back_button)
        topBarTitle.text = "러닝 챌린지"
    }
}
