package com.example.yourun.view.custom

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.yourun.R

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
        val parts = startDate.split("-") // "2025-01-22" -> ["2025", "01", "22"]
        if (parts.size == 3) {
            val month = parts[1].toInt() // "01" -> 1
            val day = parts[2] // "22"

            startDateTextView.text = "${month}월 ${day}일부터"
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
            1 -> R.drawable.ic_waiting_matching
            2 -> R.drawable.ic_progressing_matching
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

    fun updateSoloImage(tendency: String) {
        // Solo 챌린지 → 메이트의 성향에 맞는 단일 이미지 적용
        val userImage = getTendencyImage(tendency)
        imgUser1.setImageResource(userImage) // 첫 번째 프로필 이미지에만 적용
        imgUser1.isVisible = true

        // 크기 변경 (더 크게 표시)
        val layoutParams = imgUser1.layoutParams as FrameLayout.LayoutParams
        layoutParams.width = 56.dpToPx(context)  // 기존보다 크게 조정
        layoutParams.height = 56.dpToPx(context)

        // 위치 조정
        layoutParams.topMargin = 40.dpToPx(context)  // 기존보다 아래로 이동
        layoutParams.marginStart = 260.dpToPx(context)  // 기존보다 오른쪽으로 이동

        imgUser1.layoutParams = layoutParams // 변경된 값 적용

        // 기존 배경이 있으면 제거
        findViewById<View>(R.id.bgd_user_profile)?.let { removeView(it) }
    }

    /**
     *  dp 값을 px로 변환하는 확장 함수
     */
    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    fun updateCrewImages(tendencies: List<String>) {
        val imageViews = listOf(imgUser1, imgUser2, imgUser3, imgUser4)

        // 성향을 이미지 리소스로 변환
        val userImages = tendencies
            .take(4) // 최대 4명까지 표시
            .map { getTendencyImage(it) } // 성향별 이미지 매핑

        // UI에 적용
        imageViews.forEachIndexed { index, imageView ->
            if (index < userImages.size) {
                imageView.setImageResource(userImages[index])
                imageView.isVisible = true
            } else {
                imageView.isVisible = false // 멤버 수가 적으면 숨김 처리
            }
        }
    }
}
