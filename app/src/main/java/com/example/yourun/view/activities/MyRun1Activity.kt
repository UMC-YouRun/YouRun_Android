package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.databinding.ActivityMyrun1Binding

class MyRun1Activity : AppCompatActivity () {
    private lateinit var binding: ActivityMyrun1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyrun1Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnEdit.setOnClickListener{

            val intent= Intent(this, MyRun2Activity::class.java)
            startActivity(intent)
        }

    }

}