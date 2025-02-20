package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.yourun.R
import com.example.yourun.databinding.ActivityLoginBinding
import com.example.yourun.model.network.ApiClient
import com.example.yourun.viewmodel.LoginViewModel
import com.example.yourun.model.repository.LoginRepository
import com.example.yourun.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(LoginRepository(ApiClient.getApiService()))
    }
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupPasswordVisibilityToggle()
        setupLoginButton()
        setupSignupButton()

        // 카카오 로그인 버튼 클릭
        binding.imgBtnKakao.setOnClickListener {
        }

        // 로그인 결과 관찰
        viewModel.loginResult.observe(this) { result ->
            result.onSuccess { response ->
                Log.d("LoginActivity", "로그인 성공!")

                /*
                response.data?.userId?.let { userId ->
                    Log.d("LoginActivity", "로그인 응답에서 받은 userId: $userId")
                    saveUserIdToPreferences(userId)
                } ?: Log.e("LoginActivity", "🚨 로그인 응답에 userId 없음!")

                handleAppExpNavigation()
                 */

            }.onFailure { error ->
                Log.e("LoginActivity", "로그인 실패: ${error.message}")
            }
        }
    }

    private fun setupPasswordVisibilityToggle() {
        binding.editTextPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.editTextPassword.compoundDrawablesRelative[2]
                if (drawableEnd != null && event.rawX >= (binding.editTextPassword.right - drawableEnd.bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        binding.editTextPassword.inputType =
            if (isPasswordVisible) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        binding.editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null, null,
            ContextCompat.getDrawable(
                this,
                if (isPasswordVisible) R.drawable.ic_visibilityon else R.drawable.ic_visibilityoff
            ),
            null
        )

        binding.editTextPassword.setSelection(binding.editTextPassword.text?.length ?: 0)
    }

    private fun setupSignupButton() {
        binding.imgBtnSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun setupLoginButton() {
        binding.imgBtnNext.setOnClickListener {
            val email = binding.editTextNickname.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()



            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
                startActivity(Intent(this, MainActivity::class.java))

                //handleAppExpNavigation()
            }
        }
    }


       

        // 앱 설명을 이미 봤다면 MainActivity로 이동
  private fun handleAppExpNavigation() {
    val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
    val isAppExpSeen = sharedPref.getBoolean("isAppExpSeen", false)
    Log.d("LoginActivity", "isAppExpSeen: $isAppExpSeen")

    startActivity(Intent(this, AppExpActivity::class.java))


    // 앱 설명을 이미 봤다면 MainActivity로 이동
//        if (isAppExpSeen)  {
//            startActivity(Intent(this, MainActivity::class.java))
//        } else{
//            // 앱 설명을 안 봤다면 AppExpActivity로 이동
//            startActivity(Intent(this, AppExpActivity::class.java))
//
//        }

        finish() // 현재 Activity 종료하여 뒤로 가기 방지
    }

    private fun saveUserIdToPreferences(userId: Long) {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putLong("userId", userId)
            apply()
        }
        Log.d("LoginActivity", "✅ SharedPreferences에 userId 저장 완료: $userId")
    }
}