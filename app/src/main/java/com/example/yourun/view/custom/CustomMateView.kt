package com.example.yourun.view.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.example.yourun.R
import com.example.yourun.model.data.response.UserMateInfo
import com.example.yourun.viewmodel.HomeViewModel
import com.example.yourun.viewmodel.RunningViewModel

class CustomMateView<T: ViewModel> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val showHeartButton: Boolean = true // 하트 버튼 표시 여부 (기본값: 표시)
) : FrameLayout(context, attrs, defStyleAttr) {

    private val userIndex: TextView
    private val userProfile: ImageView
    private val userName: TextView
    private val userRunDay: TextView
    private val userRunDistance: TextView
    private val userTag: TextView
    private val heartButton: ImageButton
    private var isLiked = false // 현재 좋아요 상태 저장
    private var mateId: Long = -1L
    private var mateNickname: String = ""
    private var mateTendency: String = ""
    private var viewModel: T? = null
    private var isSelected = false

    var onMateSelected: ((String, String, Long) -> Unit)? = null // 닉네임, 성향, ID 전달

    init {
        LayoutInflater.from(context).inflate(R.layout.item_similar_mate, this, true)

        userIndex = findViewById(R.id.idx_challenge_item)
        userProfile = findViewById(R.id.img_user_profile)
        userName = findViewById(R.id.user_name)
        userRunDay = findViewById(R.id.user_run_day)
        userRunDistance = findViewById(R.id.user_run_distance)
        userTag = findViewById(R.id.user_tag)
        heartButton = findViewById(R.id.btn_mate_heart)

        // 하트 버튼 표시 여부에 따라 숨김 처리
        if (!showHeartButton) {
            heartButton.visibility = View.GONE
        }

        setOnClickListener {
            toggleSelection()
            onMateSelected?.invoke(mateNickname, mateTendency, mateId)
        }
    }

    fun setViewModel(viewModel: T) {
        this.viewModel = viewModel

        (viewModel as? HomeViewModel)?.likedMates?.observeForever { likedMates ->
            isLiked = likedMates.contains(mateId)
        }

        (viewModel as? RunningViewModel)?.let {
            // RunningViewModel 관련 동작 추가 가능
        }
    }

    fun updateMateInfo(mate: UserMateInfo, index: Int) {
        userIndex.text = index.toString()
        userName.text = mate.nickname
        userRunDay.text = "${mate.countDay}일째"
        userRunDistance.text = "${(mate.totalDistance / 1000)}km"
        userTag.text = mate.tags.joinToString(" ") { "#$it" }

        mateId = mate.id
        mateNickname = mate.nickname
        mateTendency = mate.tendency
        userProfile.setImageResource(getProfileImageRes(mateTendency))

        // 하트 버튼이 표시될 경우에만 클릭 이벤트 설정
        if (showHeartButton) {
            heartButton.setOnClickListener {
                isLiked = !isLiked
                heartButton.isSelected = isLiked
                if (viewModel is HomeViewModel) {
                    (viewModel as HomeViewModel).addMate(mate.id)
                }
            }
        } else {
            heartButton.setOnClickListener(null) // 클릭 리스너 해제
        }
    }

    private fun toggleSelection() {
        isSelected = !isSelected
        if (isSelected) {
            setBackgroundColor(Color.parseColor("#FFF1CD")) // 선택 시 배경 색상 변경
        } else {
            setBackgroundColor(Color.TRANSPARENT) // 해제 시 원래 배경 색으로 복귀
        }
    }

    private fun getProfileImageRes(tendency: String?): Int {
        return when (tendency) {
            "페이스메이커" -> R.drawable.img_circle_profile_facemaker
            "스프린터" -> R.drawable.img_circle_profile_sprinter
            "트레일러너" -> R.drawable.img_circle_profile_trailrunner
            else -> R.drawable.bgd_mate_profile // 기본 이미지
        }
    }
}