package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.databinding.ActivityCreateChallengeBinding
import com.example.yourun.view.fragments.CreateSolo1Fragment


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
            when {
                binding.checkboxCrew.isChecked -> {
                    val intent = Intent(this, CreateCrewActivity::class.java)
                    startActivity(intent)  // Crew 선택 시 CreateCrewActivity로 이동
                }

                binding.checkboxSolo.isChecked -> {
                    val intent = Intent(this, CreateSoloActivity::class.java)
                    startActivity(intent)  // Solo 선택 시 CreateSoloActivity로 이동 (필요하면)
                }

                else -> {
                    Toast.makeText(this, "챌린지 종류를 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
