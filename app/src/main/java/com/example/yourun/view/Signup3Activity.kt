package com.example.yourun.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.databinding.ActivitySignup3Binding

class Signup3Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySignup3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup3Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}