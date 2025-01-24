package com.example.yourun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.yourun.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val crewButton = view.findViewById<ImageButton>(R.id.btn_crew)
        val soloButton = view.findViewById<ImageButton>(R.id.btn_solo)
        var isPressed_crew = false
        var isPressed_solo = false

        crewButton.setOnClickListener {
            if (isPressed_crew) {
                crewButton.setImageResource(R.drawable.img_crew_btn_selected)
                soloButton.setImageResource(R.drawable.img_solo_btn_unselected)
            }
            isPressed_crew = !isPressed_crew
        }

        soloButton.setOnClickListener {
            if (isPressed_solo) {
                soloButton.setImageResource(R.drawable.img_solo_btn_selected)
                crewButton.setImageResource(R.drawable.img_crew_btn_unselected)
            }
            isPressed_solo = !isPressed_solo
        }

        val addChallengeButton = view.findViewById<ImageButton>(R.id.btn_add_challenge)
        val challengeView = view.findViewById<FrameLayout>(R.id.main_challenge_frame)

        addChallengeButton.setOnClickListener {
            /* 서버 데이터 가져오기
            fetchDataFromServer { data ->
                // 커스텀 뷰 생성
                val customView = CustomDataView(this).apply {
                    setData(data) // 서버 데이터 설정
                }
                val customChallengeView = findViewById<CustomChallengeView>(R.id.customChallengeView)
                customChallengeView.updateUsers(userImages)
                // 추가적인 텍스트나 상태 업데이트 (예시)
                customChallengeView.updateTexts("Team Alpha", "Max Running: 15km")
                customChallengeView.updateChallengeState(R.drawable.ic_running)

                // FrameLayout에 커스텀 뷰 추가
                challengeView.removeView(addChallengeButton) // 기존 버튼 제거
                challenge.addView(customView) // 커스텀 뷰 추가
            } */
        }


        val userImages = listOf(
            view.findViewById<ImageView>(R.id.img_user1),
            view.findViewById<ImageView>(R.id.img_user2),
            view.findViewById<ImageView>(R.id.img_user3),
            view.findViewById<ImageView>(R.id.img_user4)
        )

        /* 서버 데이터 가져오기
        fetchUserDataFromServer { users ->
            // 유저 데이터에 따라 이미지 업데이트
            userImages.forEachIndexed { index, imageView ->
                if (index < users.size && users[index].isAvailable) {
                    imageView.setImageResource(users[index].imageRes) // 유저 이미지 설정
                    imageView.visibility = View.VISIBLE // 이미지 표시
                } else {
                    imageView.visibility = View.GONE // 유저가 없으면 숨김
                }
            }
        } */

        val mainConstraintLayout = view.findViewById<ConstraintLayout>(R.id.main_constraint_layout)
        /* viewModel로 데이터 받아와서 item 추가하기
        // ViewModel 데이터 관찰
        viewModel.items.observe(viewLifecycleOwner, Observer { items ->
            addItemsToBottom(constraintLayout, items.take(5)) // 최대 5개의 아이템 추가
        })

        // 서버 데이터 로드
        viewModel.fetchDataFromServer() */

    }

    /* 메이트 아이템 추가하는 함수
    private fun addItemsToBottom(constraintLayout: ConstraintLayout, items: List<UserData>) {
        val inflater = LayoutInflater.from(requireContext())
        var lastViewId = R.id.view_home_mate // 마지막 뷰의 ID

        items.forEach { user ->
            // 아이템 레이아웃 Inflate
            val itemView = inflater.inflate(R.layout.item_layout, constraintLayout, false)
            itemView.id = View.generateViewId() // 고유 ID 설정

            // 데이터 설정
            itemView.findViewById<TextView>(R.id.idx_challenge_item).text = user.index
            itemView.findViewById<TextView>(R.id.user_name).text = user.name
            itemView.findViewById<TextView>(R.id.user_run_day).text = user.runDays
            itemView.findViewById<TextView>(R.id.user_tag).text = user.tag
            itemView.findViewById<ImageView>(R.id.img_user_profile).setImageResource(user.profileImage)

            // ConstraintLayout에 추가
            constraintLayout.addView(itemView)

            // ConstraintSet으로 레이아웃 배치
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)

            // 이전 뷰 아래에 배치
            constraintSet.connect(itemView.id, ConstraintSet.TOP, lastViewId, ConstraintSet.BOTTOM, 16)
            constraintSet.connect(itemView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 16)
            constraintSet.connect(itemView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16)

            constraintSet.applyTo(constraintLayout)

            // 마지막 뷰 ID 업데이트
            lastViewId = itemView.id
        } */
}