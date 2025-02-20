package com.example.yourun.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.yourun.R
import com.example.yourun.databinding.ActivitySignupBinding
import com.example.yourun.view.fragments.QuestionFragment
import com.example.yourun.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        val openQuestionFragment = intent.getBooleanExtra("openQuestionFragment", false)

        if (openQuestionFragment) {
            // QuestionFragment로 이동
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.questionFragment)
        }
    }
}
