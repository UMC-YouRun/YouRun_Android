package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import com.example.yourun.databinding.ActivityCreateSoloBinding
import com.example.yourun.view.fragments.CreateSolo1Fragment

class CreateSoloActivity : AppCompatActivity () {

    private lateinit var binding: ActivityCreateSoloBinding  // 바인딩 객체 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val topBarTitle: TextView = findViewById(R.id.txtTopBarWithBackButton)
        topBarTitle.text = "챌린지 생성하기"
        val calendarButton: ImageButton = findViewById(R.id.CalanderButton)
        calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        // View Binding 초기화
        binding = ActivityCreateSoloBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fragment 추가
        supportFragmentManager.beginTransaction()
            .replace(R.id.create_solo_fragment_container, CreateSolo1Fragment())
            .commit()


    }
}
