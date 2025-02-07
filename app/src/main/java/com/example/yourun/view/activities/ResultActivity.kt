//package com.example.yourun.view.activities
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import com.example.yourun.R
//import com.example.yourun.model.network.ApiClient
//import com.example.yourun.model.repository.ResultRepository
//import com.example.yourun.viewmodel.SignUpViewModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class ResultActivity : AppCompatActivity() {
//
//    private val signUpViewModel: SignUpViewModel by viewModels({ this.application })
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_result)
//
//        val topBarTitle: TextView = findViewById(R.id.txt_top_bar_with_back_button)
//        topBarTitle.text = "러닝 테스트 결과"
//
//        val finalScore = intent.getIntExtra("FINAL_SCORE", 0)
//
//        val resultType = when (finalScore) {
//            in 3..4 -> "스프린터"
//            in 5..6 -> "페이스 메이커"
//            in 7..8 -> "마라토너"
//            else -> "알 수 없음"
//        }
//
//        val resultData = ResultRepository.getResultByType(resultType)
//
//        if (resultData != null) {
//
//            val resultTitle: TextView = findViewById(R.id.resultTitle)
//            val resultSubTitle: TextView = findViewById(R.id.resultSubTitle)
//            val resultCharacter: ImageView = findViewById(R.id.resultCharacter)
//            val resultDescription: TextView = findViewById(R.id.resultDescription)
//            val startRunningButton: Button = findViewById(R.id.startRunningButton)
//
//            resultTitle.text = "러닝 성향 테스트 결과"
//            resultSubTitle.text = "당신은 ${resultData.userType}!"
//            resultCharacter.setImageResource(resultData.characterImageRes)
//            resultDescription.text = resultData.description
//
//            startRunningButton.setOnClickListener {
//                completeSignUp(resultType)
//            }
//        }
//    }
//
//    private fun completeSignUp(resultType: String) {
//        Log.d("ResultActivity", "🔍 completeSignUp 호출됨! resultType: $resultType")
//
//        val signUpRequest = signUpViewModel.getFinalData().copy(tendency = resultType)
//
//        Log.d("ResultActivity", "📦 최종 회원가입 데이터: $signUpRequest")
//
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val apiService = ApiClient.getApiService(this@ResultActivity)
//                val response = apiService.signUp(signUpRequest) // ✅ suspend 함수는 coroutine 안에서 호출해야 함
//
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@ResultActivity, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
//                    Log.d("ResultActivity", "✅ 회원가입 성공: $response")
//
//                    val intent = Intent(this@ResultActivity, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@ResultActivity, "회원가입 실패: ${e.message}", Toast.LENGTH_SHORT).show()
//                    Log.e("ResultActivity", "❌ 회원가입 실패: ${e.message}")
//                }
//            }
//        }
//    }
//}
