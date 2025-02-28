package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.yourun.R
import com.example.yourun.model.repository.QuestionRepository
import com.example.yourun.view.adapters.QuestionPagerAdapter
import com.example.yourun.viewmodel.QuestionViewModel
import com.google.android.material.button.MaterialButton

class RequestionActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var nextButton: MaterialButton
    private val questionViewModel = QuestionViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requestion)

        viewPager = findViewById(R.id.viewPager)
        nextButton = findViewById(R.id.nextButton)

        val questions = QuestionRepository.questions
        val adapter = QuestionPagerAdapter(questions) { _, score ->
            questionViewModel.addScore(score)
        }
        viewPager.adapter = adapter

        nextButton.setOnClickListener {
            val nextItem = viewPager.currentItem + 1
            if (nextItem < questions.size) {
                viewPager.currentItem = nextItem
            } else {
                val finalScore = questionViewModel.score.value ?: 0

                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("FINAL_SCORE", finalScore) // ✅ 점수 전달
                }
                startActivity(intent)
                finish()
            }
        }
    }
}
