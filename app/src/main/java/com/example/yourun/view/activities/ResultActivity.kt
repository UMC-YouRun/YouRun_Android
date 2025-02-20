package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import com.example.yourun.databinding.ActivityResultBinding
import com.example.yourun.model.repository.ResultRepository

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topBar.txtTopBarWithBackButton.text = "러닝테스트 결과"

        val finalScore = intent.getIntExtra("FINAL_SCORE", 0)

        val resultType = when (finalScore) {
            in 1..5 -> "스프린터"
            in 5..7 -> "페이스메이커"
            in 7..100 -> "트레일러너"
            else -> "알 수 없음"
        }

        val resultData = ResultRepository.getResultByType(resultType)

        resultData?.let {
            binding.resultTitle.text = "러닝 성향 테스트 결과"
            binding.resultSubTitle.text = "당신은 ${it.userType}!"
            binding.resultCharacter.setImageResource(it.characterImageRes)
            binding.resultDescription.text = it.description

            binding.startRunningButton.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java) // ✅ 메인으로 이동
                startActivity(intent)
                finish()
            }
        }
    }
}
