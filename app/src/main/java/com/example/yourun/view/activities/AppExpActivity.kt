package com.example.yourun.view.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import com.example.yourun.databinding.ActivityAppExpBinding
import com.example.yourun.view.fragments.AppExpFragment

class AppExpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppExpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppExpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val savedNickname = sharedPref.getString("nickname", null)

        if (savedInstanceState == null) {
            val fragment = AppExpFragment().apply {
                arguments = Bundle().apply {
                    putString("nickname", savedNickname)
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.app_exp_fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
