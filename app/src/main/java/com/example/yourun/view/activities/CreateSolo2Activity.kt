package com.example.yourun.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.databinding.ActivityCreateSolo2Binding


class CreateSolo2Activity  : AppCompatActivity () {
    private lateinit var binding: ActivityCreateSolo2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSolo2Binding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}