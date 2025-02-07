package com.example.yourun.view.activities

import android.Manifest
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.yourun.R
import com.example.yourun.view.fragments.HomeFragment
import com.example.yourun.view.fragments.MateFragment
import com.example.yourun.view.fragments.MyRunFragment
import com.example.yourun.view.fragments.RunningFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.yourun.view.fragments.ChallengeFragment

class MainActivity : AppCompatActivity() {

    private val locationPermissionRequestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 온보딩 완료 여부 확인 후, 처음 실행이면 온보딩 화면으로 이동
        /* if (!isOnboardingCompleted()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish() // MainActivity 종료
            return
        } */
        setContentView(R.layout.activity_main)

        checkLocationPermission()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val fabRunning = findViewById<ImageView>(R.id.fab_running)

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

    // 온보딩 완료 여부를 SharedPreferences에서 확인
    private fun isOnboardingCompleted(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isOnboardingCompleted", false)
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 이미 허용된 경우 실행할 코드
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionRequestCode
            )
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}