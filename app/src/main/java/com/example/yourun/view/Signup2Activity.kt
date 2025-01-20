package com.example.yourun.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.databinding.ActivitySignup2Binding

class Signup2Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySignup2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnNext.setOnClickListener{
            val intent= Intent(this, Signup3Activity::class.java)
            startActivity(intent)
        }
    }
}