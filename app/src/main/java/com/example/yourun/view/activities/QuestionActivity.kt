//package com.example.yourun.view.activities
//
//import android.content.Intent // 추가된 부분
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.viewpager2.widget.ViewPager2
//import com.example.yourun.R
//import com.example.yourun.model.repository.QuestionRepository
//import com.example.yourun.view.adapters.QuestionPagerAdapter
//import com.example.yourun.viewmodel.QuestionViewModel
//import com.example.yourun.viewmodel.SignUpViewModel
//
//class QuestionActivity : AppCompatActivity() {
//    private val questionViewModel: QuestionViewModel by viewModels()
//    private val signUpViewModel: SignUpViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_question)
//
//        val topBarTitle: TextView = findViewById(R.id.txt_top_bar_with_back_button)
//        topBarTitle.text = "러닝 성향 테스트"
//
//        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
//        val nextButton: Button = findViewById(R.id.nextButton)
//        val questions = QuestionRepository.questions
//
//        val adapter = QuestionPagerAdapter(questions) { _, score ->
//            questionViewModel.addScore(score)
//        }
//
//        viewPager.adapter = adapter
//
//        nextButton.setOnClickListener {
//            val nextItem = viewPager.currentItem + 1
//            if (nextItem < questions.size) {
//                viewPager.currentItem = nextItem
//            } else {
//                val finalScore = questionViewModel.score.value ?: 0
//                val intent = Intent(this, ResultActivity::class.java).apply {
//                    putExtra("FINAL_SCORE", finalScore)
//                }
//                startActivity(intent)
//                finish()
//            }
//        }
//    }
//}
