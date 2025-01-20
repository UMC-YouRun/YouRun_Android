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

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_left, null)
//        toolbar.setNavigationOnClickListener {
//            finish()
//        }

        val resultType = "페이스 메이커" // 성향테스트 로직 반영해서 받아와야 함
        val resultData = ResultRepository.getResultByType(resultType)

        if (resultData != null) {
            // 뷰 참조
            val resultTitle: TextView = findViewById(R.id.resultTitle)
            val resultSubTitle: TextView = findViewById(R.id.resultSubTitle)
            val resultCharacter: ImageView = findViewById(R.id.resultCharacter)
            val resultDescription: TextView = findViewById(R.id.resultDescription)
            val startRunningButton: Button = findViewById(R.id.startRunningButton)

            // 데이터로 UI 업데이트
            resultTitle.text = "러닝 성향 테스트 결과"
            resultSubTitle.text = "${resultData.userName}은 ${resultData.userType}!"
            resultCharacter.setImageResource(resultData.characterImageRes)
            resultDescription.text = resultData.description

            startRunningButton.setOnClickListener {
                finish()
            }
        }
    }
}
