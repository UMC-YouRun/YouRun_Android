package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.yourun.R
import com.example.yourun.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            binding.editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                ContextCompat.getDrawable(this, R.drawable.ic_visibilityon),
                null
            )
        } else {
            binding.editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                ContextCompat.getDrawable(this, R.drawable.ic_visibilityoff),
                null
            )
        }

        // 커서 위치 유지
        binding.editTextPassword.setSelection(binding.editTextPassword.text?.length ?: 0)
    }

    private fun setupLoginButton() {
        binding.imgBtnNext.setOnClickListener {
            val email = binding.editTextNickname.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 로그인 API 호출, 온보딩 화면 전환 등 실제 로그인 처리
                // 예시: 로그인 성공 시 홈화면으로 전환(홈페이지 받으면 수정해야됨)
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    private fun setupSignupButton() {
        binding.imgBtnSignup.setOnClickListener {
            val intent = Intent(this, SignUp1Activity::class.java)
            startActivity(intent)
        }
    }
    // 카카오 로그인 기능 구현은 후순위
}
