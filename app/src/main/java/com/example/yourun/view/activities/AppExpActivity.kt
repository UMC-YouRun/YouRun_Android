package com.example.yourun.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import com.example.yourun.view.fragments.AppExpFragment

class AppExpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_exp)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.app_exp_fragment_container, AppExpFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}