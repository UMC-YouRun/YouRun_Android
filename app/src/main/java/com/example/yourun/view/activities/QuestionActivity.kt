package com.example.yourun.view.activities

import android.content.Intent // 추가된 부분
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.yourun.R
import com.example.yourun.model.repository.QuestionRepository
import com.example.yourun.view.adapters.QuestionPagerAdapter

class QuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "러닝 성향 테스트"

//
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_left, null)
//        toolbar.setNavigationOnClickListener {
//            finish()
//        }

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val nextButton: Button = findViewById(R.id.nextButton)
        val questions = QuestionRepository.questions

        val adapter = QuestionPagerAdapter(questions) { questionId, selectedAnswer ->
            // 답변 선택 시 Toast 표시--> 나중에 로직 변경
//            Toast.makeText(this, "Q$questionId: $selectedAnswer", Toast.LENGTH_SHORT).show()
        }
        viewPager.adapter = adapter

        nextButton.setOnClickListener {
            val nextItem = viewPager.currentItem + 1
            if (nextItem < questions.size) {
                viewPager.currentItem = nextItem
            } else {
                val intent = Intent(this, ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
