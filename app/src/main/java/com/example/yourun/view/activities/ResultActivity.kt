package com.example.yourun.view.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import com.example.yourun.model.repository.ResultRepository

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "러닝 성향 테스트 결과"

        // FINAL_SCORE 받아오기
        val finalScore = intent.getIntExtra("FINAL_SCORE", 0)

        // 점수에 따라 성향 결정
        val resultType = when (finalScore) {
            in 3..4 -> "스프린터"
            in 5..6 -> "페이스 메이커"
            in 7..8 -> "마라토너"
            else -> "알 수 없음" // 예외 처리
        }

        val resultData = ResultRepository.getResultByType(resultType)

        if (resultData != null) {
            // 뷰 참조
            val resultTitle: TextView = findViewById(R.id.resultTitle)
            val resultSubTitle: TextView = findViewById(R.id.resultSubTitle)
            val resultCharacter: ImageView = findViewById(R.id.resultCharacter)
            val resultDescription: TextView = findViewById(R.id.resultDescription)
            val startRunningButton: Button = findViewById(R.id.startRunningButton)
            
            resultTitle.text = "러닝 성향 테스트 결과"
            resultSubTitle.text = "당신은 ${resultData.userType}!"
            resultCharacter.setImageResource(resultData.characterImageRes)
            resultDescription.text = resultData.description

            startRunningButton.setOnClickListener {
                finish()
            }
        }
    }
}
