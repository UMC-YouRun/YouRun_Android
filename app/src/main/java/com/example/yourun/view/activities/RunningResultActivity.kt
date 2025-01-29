package com.example.yourun.view.activities

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.yourun.R

class RunningResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_result)

        val titleTextView = findViewById<TextView>(R.id.txt_top_bar_with_back_button)
        titleTextView.text = "러닝 결과"

        val resultDistanceTextView = findViewById<TextView>(R.id.txt_result_distance)

        val spannable1 = SpannableString("3.26 /km") // 거리 데이터 받아오기
        spannable1.setSpan(
            AbsoluteSizeSpan(16, true), // 글자 크기: 16sp
            0, 6, // "숫자" 부분
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable1.setSpan(
            AbsoluteSizeSpan(11, true), // 글자 크기: 11sp
            6, spannable1.length, // "/km" 부분
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        resultDistanceTextView.text = spannable1

        val txtMateName = findViewById<TextView>(R.id.txt_result_name_mate)

        val fullText = txtMateName.text.toString()
        val targetText = "루시"
        val spannableString = SpannableString(fullText)
        val startIndex = fullText.indexOf(targetText)
        val endIndex = startIndex + targetText.length

        if (startIndex >= 0) {
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_purple)),
                startIndex,
                endIndex,
                SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        txtMateName.text = spannableString
    }
}