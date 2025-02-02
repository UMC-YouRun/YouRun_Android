package com.example.yourun.view.activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R

class ResultSoloActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_solo)

        val btnConfirm = findViewById<ImageButton>(R.id.btn_confirm)

        val topBarTitle: TextView = findViewById(R.id.txt_top_bar_with_back_button)
        topBarTitle.text = "개인 챌린지 결과"

        btnConfirm.setOnClickListener {
            finish()
        }
    }
}
