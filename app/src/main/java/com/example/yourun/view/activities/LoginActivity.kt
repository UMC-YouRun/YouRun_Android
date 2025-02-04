package com.example.yourun.view.activities


import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.databinding.ActivityLoginBinding
import com.example.yourun.model.data.LoginRequest
import com.example.yourun.model.data.LoginResponse
import com.example.yourun.model.network.ApiService
import com.example.yourun.model.network.RetrofitClient
import com.example.yourun.model.network.TokenManager
import kotlinx.coroutines.launch
import retrofit2.Call


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var apiService: ApiService
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenManager.getInstance(this)
        apiService = RetrofitClient.create(this)

        setupPasswordVisibilityToggle()
        setupLoginButton()
        setupSignupButton()
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

        if (isPasswordVisible) {
            binding.editTextPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                ContextCompat.getDrawable(this, R.drawable.ic_visibilityon),
                null
            )
        } else {
            binding.editTextPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                ContextCompat.getDrawable(this, R.drawable.ic_visibilityoff),
                null
            )
        }

        binding.editTextPassword.setSelection(binding.editTextPassword.text?.length ?: 0)
    }

    private fun setupSignupButton() {
        binding.imgBtnSignup.setOnClickListener {
            val intent = Intent(this, SignUp1Activity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLoginButton() {
        binding.imgBtnNext.setOnClickListener {
            val email = binding.editTextNickname.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                login(email, password)
            }
        }
    }

    private suspend fun login(email: String, password: String) {
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
}