package com.example.yourun.view.activities

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
                    binding.imgBtnSignupSuccess.isEnabled = isValidNickname(nickname)
                }
            }
        })

        // btnBack 클릭 이벤트
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // imgBtnSignupSuccess 클릭 이벤트 (버튼 눌렀을 때 페이지 전환)
        binding.imgBtnSignupSuccess.setOnClickListener {
            val nickname = binding.editTextNickname.text.toString()
            if (isValidNickname(nickname)) {
                // 홈 페이지로 넘어가는 코드 짜기
                // 예: startActivity(Intent(this, NextActivity::class.java))
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
        checkBox.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                selectedCount ++
            }
            else {
                selectedCount--
            }

            if (selectedCount > 2){
                Toast.makeText(this, "2개만 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                checkBox.isChecked = false
                selectedCount--
            }
            if(selectedCount == 1){
                Toast.makeText(this, "2개 선택 필수입니다.", Toast.LENGTH_SHORT).show()


            }
        }
    }


}
