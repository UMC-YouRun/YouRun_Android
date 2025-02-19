package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import com.example.yourun.databinding.ActivityMyrunBinding
import com.example.yourun.view.fragments.MyRunFragment

class MyRunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyrunBinding  // 바인딩 객체 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding 초기화
        binding = ActivityMyrunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fragment 추가
        supportFragmentManager.beginTransaction()
            .replace(R.id.my_run_fragment_container, MyRunFragment())
            .commit()





    }
}