package com.example.yourun.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.yourun.databinding.ActivitySignupBinding
import com.example.yourun.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
    }
}
