package com.example.yourun.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.databinding.ActivityMyrun2Binding

class MyRun2Activity : AppCompatActivity () {
    private lateinit var binding: ActivityMyrun2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyrun2Binding.inflate(layoutInflater)
        setContentView(binding.root)




    }
}