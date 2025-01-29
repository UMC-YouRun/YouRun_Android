package com.example.yourun.view.activities

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.yourun.R
import com.example.yourun.view.fragments.ChallengeFragment
import com.example.yourun.view.fragments.HomeFragment
import com.example.yourun.view.fragments.MateFragment
import com.example.yourun.view.fragments.MyRunFragment
import com.example.yourun.view.fragments.RunningFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val fabRunning = findViewById<ImageView>(R.id.fab_running)
        val fabTitle = findViewById<TextView>(R.id.fab_title)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, HomeFragment())
            .commit()

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.nav_mate -> {
                    loadFragment(MateFragment())
                    true
                }

                R.id.nav_challenge -> {
                    loadFragment(ChallengeFragment())
                    true
                }

                R.id.nav_my -> {
                    loadFragment(MyRunFragment())
                    true
                }

                else -> false
            }
        }

        fabRunning.setOnClickListener {
            changeFabColor(RunningFragment())
            loadFragment(RunningFragment())
            val menu = bottomNavigationView.menu
            menu.getItem(2).isChecked = true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        changeFabColor(fragment)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
            .commit()
    }

    private fun changeFabColor(fragment: Fragment) {
        val fabRunning = findViewById<ImageView>(R.id.fab_running)
        val fabTitle = findViewById<TextView>(R.id.fab_title)

        if (fragment is RunningFragment) {
            fabRunning.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.yellow))
            fabRunning.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.round_button_background))
            fabTitle.setTextColor(ContextCompat.getColor(this, R.color.round_button_background))
        }
        else {
            fabRunning.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.round_button_background))
            fabRunning.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.round_button))
            fabTitle.setTextColor(ContextCompat.getColor(this, R.color.round_button))
        }
    }
}

