package com.example.yourun.view.custom

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.yourun.R
import com.example.yourun.model.data.Tendency

class CustomHomeChallenge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val imgUser1: ImageView
    private val imgUser2: ImageView
    private val imgUser3: ImageView
    private val imgUser4: ImageView
    private val challengeStateIcon: ImageView
    private val crewAndMateTitle: TextView
    private val startDateTextView: TextView
    private val dayCountTextView: TextView
    private val challengePeriodTextView: TextView
    private val challengeDistanceTextview: TextView

    init {
        // XML 레이아웃을 inflate하여 뷰에 추가
        LayoutInflater.from(context).inflate(R.layout.custom_home_challenge, this, true)

        // 뷰 참조 초기화
        imgUser1 = findViewById(R.id.img_user1)
        imgUser2 = findViewById(R.id.img_user2)
        imgUser3 = findViewById(R.id.img_user3)
        imgUser4 = findViewById(R.id.img_user4)
        challengeStateIcon = findViewById(R.id.ic_challenge_state)
        crewAndMateTitle = findViewById(R.id.crewAndMateTitle)
        startDateTextView = findViewById(R.id.startDate)
        dayCountTextView = findViewById(R.id.dayCount)
        challengePeriodTextView = findViewById(R.id.challengePeriod)
        challengeDistanceTextview = findViewById(R.id.challengeDistance)
    }

    fun updateSoloTitle(mateName: String) {
        val fullText = "챌린지 메이트 $mateName"
        val spannable = SpannableString(fullText)

        // mateName 부분만 색상 변경
        val color = ContextCompat.getColor(context, R.color.text_purple)
        spannable.setSpan(
            ForegroundColorSpan(color), // 색상 적용
            8, fullText.length, // crewName만 적용
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        crewAndMateTitle.text = spannable // 적용된 SpannableString 설정
    }

    fun updateCrewTitle(crewName: String) {
        val fullText = "$crewName 크루"
        val spannable = SpannableString(fullText)

        // crewName 부분만 색상 변경
        val color = ContextCompat.getColor(context, R.color.text_purple)
        spannable.setSpan(
            ForegroundColorSpan(color), // 색상 적용
            0, crewName.length, // crewName만 적용
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        crewAndMateTitle.text = spannable // 적용된 SpannableString 설정
    }

    // 날짜 업데이트
    fun updateDates(startDate: String, dayCount: Int) {
        try {
            val parts = startDate.split("-")
            val month = parts[1].toIntOrNull() ?: 0
            val day = parts[2].toIntOrNull() ?: 0

            startDateTextView.text = "${month}월 ${day}일부터"

        } catch (e: Exception) {
            Log.e("CustomHomeChallenge", "날짜 변환 오류: ${e.message}")
            startDateTextView.text = "날짜 오류"
        }

        dayCountTextView.text = "${dayCount}일째"
    }

    // 기간 업데이트
    fun updatePeriodCrew(period: Int) {
        challengePeriodTextView.text = "${period}일 동안"
    }

    // 기간 업데이트
    fun updatePeriodSolo(period: Int) {
        challengePeriodTextView.text = "${period}일 연속"
    }

    fun updateDistance(distance: Int) {
        challengeDistanceTextview.text = "${distance}km 러닝!"
    }

    // 챌린지 상태 아이콘 업데이트
    fun updateChallengeState(challengeLevel: Int) {
        val drawableRes = when (challengeLevel) {
            0 -> R.drawable.ic_waiting_matching
            1 -> R.drawable.ic_progressing_matching
            2 -> R.drawable.ic_complete
            else -> R.drawable.ic_waiting_matching // 기본값
        }
        challengeStateIcon.setImageResource(drawableRes)
    }

    private fun getTendencyImage(tendency: String): Int {
        return when (tendency) {
            "페이스메이커" -> R.drawable.img_circle_profile_facemaker
            "스프린터" -> R.drawable.img_circle_profile_sprinter
            "트레일러너" -> R.drawable.img_circle_profile_trailrunner
            else -> R.drawable.img_circle_profile_facemaker // 기본 이미지
        }
    }

    fun updateSoloImage(tendency: String?) {
        if (tendency == null) {
            Log.e("CustomHomeChallenge", "tendency 값이 null입니다.")
            return
        }
        val userImage = getTendencyImage(tendency)
        imgUser1.setImageResource(userImage)
        imgUser1.isVisible = true

        // 크기 및 위치 수정
        imgUser1.layoutParams = imgUser1.layoutParams.apply {
            width = 56.dpToPx(context)
            height = 56.dpToPx(context)
        }

        // 강제로 레이아웃 업데이트
        imgUser1.requestLayout()

        // 기존 배경이 있으면 숨김 처리
        findViewById<View>(R.id.bgd_user_profile)?.isVisible = false
    }

    /**
     *  dp 값을 px로 변환하는 확장 함수
     */
    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    fun updateCrewImages(tendencies: List<Tendency>?) {
        if (tendencies == null) {
            Log.e("CustomHomeChallenge", "tendency 값이 null입니다.")
            return
        }
        val imageViews = listOf(imgUser1, imgUser2, imgUser3, imgUser4)

        // 최대 4명까지만 표시
        val userImages = tendencies.map { getTendencyImage(it.value) }

        // UI에 적용
        imageViews.forEachIndexed { index, imageView ->
            if (index < userImages.size) {
                imageView.setImageResource(userImages[index])
                imageView.isVisible = true
            } else {
                imageView.isVisible = false
            }
        }
    }
}
