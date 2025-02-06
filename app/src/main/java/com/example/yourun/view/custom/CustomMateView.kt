package com.example.yourun.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.yourun.R
import com.example.yourun.model.data.UserMateInfo
import com.example.yourun.viewmodel.HomeViewModel

class CustomMateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
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
    private var viewModel: HomeViewModel? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.item_similar_mate, this, true)

        userIndex = findViewById(R.id.idx_challenge_item)
        userProfile = findViewById(R.id.img_user_profile)
        userName = findViewById(R.id.user_name)
        userRunDay = findViewById(R.id.user_run_day)
        userRunDistance = findViewById(R.id.user_run_distance)
        userTag = findViewById(R.id.user_tag)
        heartButton = findViewById(R.id.btn_mate_heart)
    }

    fun setViewModel(viewModel: HomeViewModel) {
        this.viewModel = viewModel

        // 좋아요 상태를 ViewModel에서 가져와 반영
        viewModel.likedMates.observeForever { likedMates ->
            isLiked = likedMates.contains(mateId)
        }
    }

    fun updateMateInfo(mate: UserMateInfo, index: Int) {
        userIndex.text = index.toString()
        userName.text = mate.nickname
        userRunDay.text = "${mate.countDay}일째"
        userRunDistance.text = "${mate.totalDistance}km"
        userTag.text = mate.tags.joinToString(", ") // 태그 리스트를 문자열로 변환

        // 유저 성향에 따라 이미지 변경
        val profileImageRes = when (mate.tendency) {
            "페이스메이커" -> R.drawable.img_circle_profile_facemaker
            "스프린터" -> R.drawable.img_circle_profile_sprinter
            "트레일러너" -> R.drawable.img_circle_profile_trailrunner
            else -> R.drawable.img_circle_profile_facemaker // 기본 이미지
        }
        userProfile.setImageResource(profileImageRes)

        heartButton.setOnClickListener {
            viewModel?.addMate(mate.id)
        }
    }
}