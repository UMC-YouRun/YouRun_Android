package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R

class CreateChallengeActivity : AppCompatActivity() {

    private lateinit var checkBoxCrew: CheckBox
    private lateinit var checkBoxSolo: CheckBox
    private lateinit var btnChoice: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_challenge)


        checkBoxCrew = findViewById(R.id.checkbox_crew)
        checkBoxSolo = findViewById(R.id.checkbox_solo)
        btnChoice = findViewById(R.id.next_btn)


        checkBoxCrew.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBoxSolo.isChecked = false
            }
        }

        checkBoxSolo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBoxCrew.isChecked = false
            }
        }


        btnChoice.setOnClickListener {
            if (checkBoxCrew.isChecked) {
                val intent = Intent(this, CreateCrew1Activity::class.java)
                startActivity(intent)
            } else if (checkBoxSolo.isChecked) {
                val intent = Intent(this, CreateSolo1Activity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "챌린지 종류를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}