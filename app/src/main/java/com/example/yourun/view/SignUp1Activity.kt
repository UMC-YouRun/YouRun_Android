package com.example.yourun.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.databinding.ActivitySignup1Binding

class SignUp1Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySignup1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener{

            val intent= Intent(this, SignUp2Activity::class.java)
            startActivity(intent)
        }

    }
}