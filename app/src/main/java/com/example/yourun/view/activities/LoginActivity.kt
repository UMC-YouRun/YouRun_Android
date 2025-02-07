package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.text.InputType
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.yourun.R
import com.example.yourun.databinding.ActivityLoginBinding
import com.example.yourun.viewmodel.LoginViewModel
import com.example.yourun.model.repository.LoginRepository
import com.example.yourun.utils.TokenManager
import com.example.yourun.model.network.RetrofitClient
import com.example.yourun.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(LoginRepository(RetrofitClient.create(this), TokenManager.getInstance(this)))
    }
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPasswordVisibilityToggle()
        setupLoginButton()
        setupSignupButton()

        // 로그인 결과 관찰
        viewModel.loginResult.observe(this, Observer { result ->
            result.onSuccess { token ->
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AppExpActivity::class.java))
                finish()
            }
            result.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })
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
            ContextCompat.getDrawable(this, if (isPasswordVisible) R.drawable.ic_visibilityon else R.drawable.ic_visibilityoff),
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
                viewModel.login(email, password) // ViewModel을 통해 로그인 요청
            }
        }
    }
}

/*private suspend fun login(email: String, password: String) {
    try {
        val request = LoginRequest(email, password)
        val response = apiService.login(request) // suspend 함수 호출

        if (response.isSuccessful) {
            val body = response.body()
            if (body?.status == 200 && body.data?.access_token != null) {
                val token = body.data.access_token
                tokenManager.saveToken(token)
                Log.d("LoginActivity", "토큰 저장됨: $token")

                Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@LoginActivity, AppExpActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "로그인 실패: ${body?.message ?: "알 수 없는 오류"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this@LoginActivity,
                "로그인 실패: 서버 오류(${response.code()})",
                Toast.LENGTH_SHORT
            ).show()
        }
    } catch (e: Exception) {
        Toast.makeText(this@LoginActivity, "네트워크 오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
}*/