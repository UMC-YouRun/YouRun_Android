package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.databinding.ActivitySignup3Binding

class SignUp3Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySignup3Binding
    private var selectedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // 로그인 시 닉네임이 저장되어 있으면 AppExpActivity로 자동 이동
        val savedNickname = sharedPreferences.getString("nickname", null)
        if (savedNickname != null) {
            startActivity(Intent(this, AppExpActivity::class.java))
            finish() // 현재 액티비티 종료
        }

        setCheckBoxListener(binding.checkbox1)
        setCheckBoxListener(binding.checkbox2)
        setCheckBoxListener(binding.checkbox3)
        setCheckBoxListener(binding.checkbox4)
        setCheckBoxListener(binding.checkbox5)
        setCheckBoxListener(binding.checkbox6)

        binding.editTextNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val nickname = editable.toString()
                if (isValidNickname(nickname)) {
                    binding.imgBtnSignupSuccess.isEnabled = true
                }
            }
        })

        // btnBack 클릭 이벤트
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // imgBtnSignupSuccess 클릭 이벤트 (닉네임 저장 + 로그인 시 자동 이동)
        binding.imgBtnSignupSuccess.setOnClickListener {
            val nickname = binding.editTextNickname.text.toString()
            if (isValidNickname(nickname)) {
                // SharedPreferences에 닉네임 저장
                sharedPreferences.edit().putString("nickname", nickname).apply()

                // 로그인 후 AppExpActivity로 이동
                startActivity(Intent(this, AppExpActivity::class.java))
                finish() // 현재 액티비티 종료
            } else {
                Toast.makeText(this, "닉네임은 한글 2~4자만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidNickname(nickname: String): Boolean {
        val regex = "^[가-힣]{2,4}$".toRegex()
        return regex.matches(nickname)
    }

    private fun setCheckBoxListener(checkBox: CheckBox) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedCount++
            } else {
                selectedCount--
            }

            if (selectedCount > 2) {
                Toast.makeText(this, "2개만 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                checkBox.isChecked = false
                selectedCount--
            }
            if (selectedCount == 1) {
                Toast.makeText(this, "2개 선택 필수입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

