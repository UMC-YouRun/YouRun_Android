package com.example.yourun.view.activities

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R

class RunningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running)

        val titleTextView = findViewById<TextView>(R.id.txt_top_bar_with_back_button)
        titleTextView.text = "러닝"

        val runningDistanceTextView = findViewById<TextView>(R.id.txt_running_distance)

        val spannable1 = SpannableString("3.26 km") // 거리 데이터 받아오기
        spannable1.setSpan(
            AbsoluteSizeSpan(42, true), // 글자 크기: 42sp
            0, 5, // "숫자" 부분
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable1.setSpan(
            AbsoluteSizeSpan(25, true), // 글자 크기: 25sp
            5, spannable1.length, // "km" 부분
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        runningDistanceTextView.text = spannable1

        val runningTimeTextView = findViewById<TextView>(R.id.txt_running_time)

        val spannable2 = SpannableString("23.0 분") // 시간 데이터 받아오기
        spannable2.setSpan(
            AbsoluteSizeSpan(16, true), // 글자 크기: 16sp
            0, 5, // "숫자" 부분
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable2.setSpan(
            AbsoluteSizeSpan(11, true), // 글자 크기: 11sp
            5, spannable2.length, // "분" 부분
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        runningTimeTextView.text = spannable2

        val smallRunningDistanceTextView = findViewById<TextView>(R.id.txt_small_running_distance)

        val spannable3 = SpannableString("3.24 /km") // 시간 데이터 받아오기
        spannable3.setSpan(
            AbsoluteSizeSpan(16, true), // 글자 크기: 16sp
            0, 4, // "숫자" 부분
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable3.setSpan(
            AbsoluteSizeSpan(11, true), // 글자 크기: 11sp
            4, spannable3.length, // "/km" 부분
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        smallRunningDistanceTextView.text = spannable3

        val playPauseButton = findViewById<ImageButton>(R.id.btn_running_play_pause)

        playPauseButton.setOnClickListener {
            // 선택 상태를 토글
            playPauseButton.isSelected = !playPauseButton.isSelected
        }

    }
}