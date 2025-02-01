package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log

import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.yourun.R
import com.example.yourun.databinding.ActivitySignup1Binding
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class SignUp1Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySignup1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPasswordVisibilityToggle()
        setupPasswordCheck()

        binding.btnNext.setOnClickListener {
            val email = binding.etId.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val passwordCheck = binding.editTextPasswordCheck.text.toString().trim()

            if (!isEmailValid(email)) {
                Toast.makeText(this, "유효한 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isPasswordValid(password)) {
                Toast.makeText(this, "비밀번호는 영문과 숫자를 포함한 8~10자여야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != passwordCheck) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, SignUp2Activity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnDuplicate.setOnClickListener {
            val email = binding.etId.text.toString().trim()

            if (!isEmailValid(email)) {
                Toast.makeText(this, "유효한 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이메일 중복 확인 API 호출
            //checkEmailDuplicate(email)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"
        val regex = Pattern.compile(pattern)
        return regex.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        val pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,10}$"
        val regex = Pattern.compile(pattern)
        return regex.matcher(password).matches()
    }

    private fun setupPasswordVisibilityToggle() {
        binding.etPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.etPassword.compoundDrawablesRelative[2]
                if (drawableEnd != null && event.rawX >= (binding.etPassword.right - drawableEnd.bounds.width())) {
                    togglePasswordVisibility(
                        binding.etPassword,
                        R.drawable.ic_visibilityon,
                        R.drawable.ic_visibilityoff
                    )
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.editTextPasswordCheck.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.editTextPasswordCheck.compoundDrawablesRelative[2]
                if (drawableEnd != null && event.rawX >= (binding.editTextPasswordCheck.right - drawableEnd.bounds.width())) {
                    togglePasswordVisibility(
                        binding.editTextPasswordCheck,
                        R.drawable.ic_visibilityon,
                        R.drawable.ic_visibilityoff
                    )
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun togglePasswordVisibility(
        editText: EditText,
        visibilityOnDrawable: Int,
        visibilityOffDrawable: Int
    ) {
        val isVisible =
            editText.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

        if (isVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null,
                ContextCompat.getDrawable(this, visibilityOffDrawable), null
            )
        } else {
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null,
                ContextCompat.getDrawable(this, visibilityOnDrawable), null
            )
        }

        editText.setSelection(editText.text?.length ?: 0)
    }

    private fun setupPasswordCheck() {
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString().trim()
                if (isPasswordValid(password)) {
                    binding.passwordContent.apply {
                        text = "사용 가능한 비밀번호입니다."
                        setTextColor(ContextCompat.getColor(this@SignUp1Activity, R.color.purple))
                    }
                } else {
                    binding.passwordContent.apply {
                        text = "조건에 맞지 않는 비밀번호입니다."
                        setTextColor(ContextCompat.getColor(this@SignUp1Activity, R.color.red))
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editTextPasswordCheck.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = binding.etPassword.text.toString().trim()
                val passwordCheck = s.toString().trim()

                if (password == passwordCheck) {
                    binding.passwordCheckContent.apply {
                        text = "비밀번호 확인 완료"
                        setTextColor(ContextCompat.getColor(this@SignUp1Activity, R.color.purple))
                    }
                } else {
                    binding.passwordCheckContent.apply {
                        text = "비밀번호가 일치하지 않습니다."
                        setTextColor(ContextCompat.getColor(this@SignUp1Activity, R.color.red))
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    /*private fun checkEmailDuplicate(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.getApiService(this@SignUp1Activity).checkEmailDuplicate(email)

                // 응답 확인 로그 추가
                Log.d("API Response", "Response: $response")

                withContext(Dispatchers.Main) {
                    if (response != null && response.status == 200 && response.data) {
                        // 중복되지 않은 이메일인 경우
                        binding.tvEmailDuplicate.apply {
                            text = "사용 가능한 이메일입니다."
                            setTextColor(ContextCompat.getColor(this@SignUp1Activity, R.color.purple))
                        }
                        binding.btnNext.isEnabled = true
                    } else {
                        // 이미 존재하는 이메일인 경우
                        binding.tvEmailDuplicate.apply {
                            text = "이미 사용 중인 이메일입니다."
                            setTextColor(ContextCompat.getColor(this@SignUp1Activity, R.color.red))
                        }
                        binding.btnNext.isEnabled = false
                    }
                }
            } catch (e: Exception) {
                // 예외 처리
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SignUp1Activity, "에러가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }*/

}
