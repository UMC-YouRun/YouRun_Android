package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import com.example.yourun.databinding.ActivityCreateChallengeBinding


class CreateChallengeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateChallengeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Crew 체크박스가 체크되면 Solo 체크박스 해제
        binding.checkboxCrew.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkboxSolo.isChecked = false
            }
        }

        // Solo 체크박스가 체크되면 Crew 체크박스 해제
        binding.checkboxSolo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkboxCrew.isChecked = false
            }
        }

        // 다음 버튼 클릭 시
        binding.nextBtn.setOnClickListener {
            // Crew 체크박스가 체크되어 있으면 CreateCrew1Activity로 이동
            if (binding.checkboxCrew.isChecked) {
                val intent = Intent(this, CreateCrew1Activity::class.java)
                startActivity(intent)
            }
            // Solo 체크박스가 체크되어 있으면 CreateSolo1Activity로 이동
            else if (binding.checkboxSolo.isChecked) {
                val intent = Intent(this, CreateSolo1Activity::class.java)
                startActivity(intent)
            }
            // 체크박스가 선택되지 않으면 경고 메시지 표시
            else {
                Toast.makeText(this, "챌린지 종류를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
