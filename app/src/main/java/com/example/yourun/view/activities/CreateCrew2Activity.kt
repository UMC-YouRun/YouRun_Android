package com.example.yourun.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.databinding.ActivityCreateCrew1Binding
import com.example.yourun.databinding.ActivityCreateCrew2Binding

class CreateCrew2Activity  : AppCompatActivity () {
    private lateinit var binding: ActivityCreateCrew2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCrew2Binding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}