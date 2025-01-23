package com.example.yourun.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
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

    init {
        // XML 레이아웃을 inflate하여 뷰에 추가
        LayoutInflater.from(context).inflate(R.layout.custom_home_challenge, this, true)

        // 뷰 참조 초기화
        imgUser1 = findViewById(R.id.img_user1)
        imgUser2 = findViewById(R.id.img_user2)
        imgUser3 = findViewById(R.id.img_user3)
        imgUser4 = findViewById(R.id.img_user4)
        challengeStateIcon = findViewById(R.id.ic_challenge_state)
    }

    /**
     * 유저 데이터를 기반으로 이미지를 업데이트합니다.
     * @param userImages 참여한 유저의 이미지 리소스 ID 리스트 (최대 4명)
     */
    fun updateUsers(userImages: List<Int>) {
        val imageViews = listOf(imgUser1, imgUser2, imgUser3, imgUser4)
        imageViews.forEachIndexed { index, imageView ->
            if (index < userImages.size) {
                imageView.setImageResource(userImages[index]) // 동적으로 유저 이미지 설정
                imageView.isVisible = true // 이미지 표시
            } else {
                imageView.isVisible = false // 남은 이미지는 숨김
            }
        }
    }

    /**
     * 챌린지 상태 아이콘을 업데이트합니다.
     * @param iconRes 챌린지 상태 아이콘 리소스 ID
     */
    fun updateChallengeState(iconRes: Int) {
        challengeStateIcon.setImageResource(iconRes)
    } // 서버에서 챌린지 진행 상황 데이터를 받아서 업데이트

    /**
     * 텍스트 업데이트
     * @param crewText 크루 이름 텍스트
     * @param maxRunningText 최대 러닝 거리 텍스트
     */
    /* fun updateTexts(crewText: String, maxRunningText: String) {
        textCrew.text = crewText
        textMaxRunning.text = maxRunningText
    } */

}