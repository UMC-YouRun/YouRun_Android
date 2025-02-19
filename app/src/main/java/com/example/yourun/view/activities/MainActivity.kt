package com.example.yourun.view.activities

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.yourun.R
// import com.example.yourun.view.fragments.HomeFragment
import com.example.yourun.view.fragments.MateFragment
import com.example.yourun.view.fragments.MyRunFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.yourun.view.fragments.ChallengeFragment
import com.example.yourun.view.fragments.ChallengeListFragment
import com.example.yourun.view.fragments.RunningFragment
import com.google.android.material.snackbar.Snackbar

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
            /*.replace(R.id.main_fragment_container
            , HomeFragment())
             */
            .commit()

        bottomNavigationView.setOnItemSelectedListener { item ->
            val newFragment = when (item.itemId) {
                /*
                R.id.nav_home -> HomeFragment()
                 */
                R.id.nav_mate -> MateFragment()
                R.id.nav_challenge -> ChallengeListFragment()
                R.id.nav_my -> MyRunFragment()
                else -> return@setOnItemSelectedListener false
            }

            // 현재 표시된 Fragment와 같으면 무시
            val currentFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container)
            if (currentFragment?.javaClass == newFragment.javaClass) {
                return@setOnItemSelectedListener false
            }

            loadFragment(newFragment)
            true
        }

        fabRunning.setOnClickListener {
            if (isActivityRunning(this, RunningActivity::class.java)) {
                // RunningActivity가 백그라운드에 있으면 복원
                val intent = Intent(this, RunningActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) // 기존 액티비티를 최상단으로 이동
                startActivity(intent)
            } else {
                changeFabColor(RunningFragment())
                loadFragment(RunningFragment())
            }
            val menu = bottomNavigationView.menu
            menu.getItem(2).isChecked = true
        }
    }

    fun isActivityRunning(context: Context, activityClass: Class<*>): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.appTasks // 현재 실행 중인 모든 태스크 가져오기

        for (task in runningTasks) {
            if (task.taskInfo.baseActivity?.className == activityClass.name) {
                return true // RunningActivity가 실행 중이라면 true 반환
            }
        }
        return false // 실행 중이 아니라면 false 반환
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
        val permission = Manifest.permission.ACCESS_FINE_LOCATION

        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                // 권한이 이미 허용된 경우 실행할 코드
            }

            shouldShowRequestPermissionRationale(permission) -> {
                AlertDialog.Builder(this)
                    .setTitle("위치 권한 필요")
                    .setMessage("앱에서 위치 권한을 허용해야 러닝 기능을 사용할 수 있습니다.")
                    .setPositiveButton("확인") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(permission),
                            locationPermissionRequestCode
                        )
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }

            else -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    locationPermissionRequestCode
                )
            }
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 허용됨
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "위치 권한이 필요합니다. 설정에서 권한을 허용해주세요.",
                    Snackbar.LENGTH_LONG
                ).setAction("설정") {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }
                    startActivity(intent)
                }.show()
            }
        }
    }
}