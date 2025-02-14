package com.example.yourun.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import com.example.yourun.databinding.ActivityCreateCrewBinding
import com.example.yourun.databinding.ActivityMyrunBinding
import com.example.yourun.view.fragments.CreateCrew1Fragment
import com.example.yourun.view.fragments.CreateCrew2Fragment
import com.example.yourun.view.fragments.MyRunFragment

class CreateCrewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCrewBinding  // 바인딩 객체 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding 초기화
        binding = ActivityCreateCrewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fragment 추가
        supportFragmentManager.beginTransaction()
            .replace(R.id.create_crew_fragment_container, CreateCrew1Fragment())
            .commit()



    }
}