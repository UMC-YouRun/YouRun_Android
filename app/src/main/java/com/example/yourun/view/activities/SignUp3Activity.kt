package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.databinding.ActivitySignup3Binding
import com.example.yourun.viewmodel.SignUpViewModel

class SignUp3Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySignup3Binding
    private val signUpViewModel: SignUpViewModel by viewModels()
    private var selectedTags = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setCheckBoxListener(binding.checkbox1, "느긋하게")
        setCheckBoxListener(binding.checkbox2, "음악과")
        setCheckBoxListener(binding.checkbox3, "열정적")
        setCheckBoxListener(binding.checkbox4, "자기계발")
        setCheckBoxListener(binding.checkbox5, "에너자이저")
        setCheckBoxListener(binding.checkbox6, "왕초보")

        // 닉네임 입력 시 ViewModel 업데이트
        binding.editTextNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val nickname = editable.toString()
                if (isValidNickname(nickname)) {
                    binding.imgBtnSignupSuccess.isEnabled = true
                    signUpViewModel.updateNicknameAndTendency(nickname, "페이스메이커") // 기본 성향 추가
                }
            }
        })

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // 회원가입 완료 버튼 클릭
        binding.imgBtnSignupSuccess.setOnClickListener {
            val nickname = binding.editTextNickname.text.toString()
            if (isValidNickname(nickname) && selectedTags.size == 2) {
                signUpViewModel.updateTags(selectedTags[0], selectedTags[1])
                val intent = Intent(this, QuestionActivity::class.java)
                startActivity(intent)
                finish()            } else {
                Toast.makeText(this, "닉네임은 한글 2~4자만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidNickname(nickname: String): Boolean {
        val regex = "^[가-힣]{2,4}$".toRegex()
        return regex.matches(nickname)
    }

    private fun setCheckBoxListener(checkBox: CheckBox, tag: String) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (selectedTags.size < 2) {
                    selectedTags.add(tag)
                } else {
                    Toast.makeText(this, "2개만 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    checkBox.isChecked = false
                }
            } else {
                selectedTags.remove(tag)
            }

            if (selectedTags.size == 1) {
                Toast.makeText(this, "2개 선택 필수입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
