package com.example.yourun.view.activities



import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.yourun.R
import com.example.yourun.databinding.ActivityLoginBinding
import com.example.yourun.model.data.LoginRequest
import com.example.yourun.model.data.LoginResponse
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private fun setupLoginButton() {
        binding.imgBtnNext.setOnClickListener {
            val email = binding.editTextNickname.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                login(email, password)
            }
        }
    }

    private fun login(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.getApiService(this@LoginActivity).login(loginRequest)
                withContext(Dispatchers.Main) {
                    if (response.status == 200 && response.data != null) {
                        // 로그인 성공 시 처리
                        handleLoginSuccess(response.data)
                    } else {
                        // 로그인 실패 시
                        val errorMessage = response.message ?: "알 수 없는 오류가 발생했습니다."
                        Toast.makeText(this@LoginActivity, "로그인 실패: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginError", "로그인 실패 - 예외: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "로그인 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleLoginSuccess(responseData: LoginResponse?) {
        val accessToken = responseData?.data?.access_token
        if (accessToken?.isNotEmpty() == true) {
            // 액세스 토큰 저장
            saveTokensToSharedPreferences(accessToken)
            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()

            // 성공 후 화면 전환
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            // 액세스 토큰이 없을 경우
            Log.e("LoginError", "로그인 실패 - Access Token이 누락되었습니다.")
            Toast.makeText(this, "로그인 실패: 토큰이 누락되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveTokensToSharedPreferences(accessToken: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("access_token", accessToken)
        editor.apply()

        val savedToken = sharedPreferences.getString("access_token", null)
        Log.d("LoginDebug", "Saved Token: $savedToken")
    }

    private fun setupSignupButton() {
        binding.imgBtnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
