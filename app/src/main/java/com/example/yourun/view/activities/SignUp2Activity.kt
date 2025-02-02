package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.databinding.ActivitySignup2Binding
import com.example.yourun.viewmodel.SignUpViewModel

class SignUp2Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySignup2Binding
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.btnNext.isEnabled = true
            } else {
                binding.btnNext.isEnabled = false
            }
        }

        binding.btnNext.setOnClickListener{
            if(binding.myCheckBox.isChecked) {
                val intent = Intent(this, SignUp3Activity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "약관 동의가 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
