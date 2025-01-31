package com.example.yourun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.yourun.R

class RunningMateSelectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_running_mate_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topBarView = view.findViewById<View>(R.id.mate_select_top_bar)
        val titleTextView = topBarView.findViewById<TextView>(R.id.txt_top_bar_with_back_button)
        titleTextView.text = "러닝메이트 선택"

        view.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 나의 러닝 메이트 텍스트 밑에 추가
        val mateSelectConstraintLayout = view.findViewById<ConstraintLayout>(R.id.mate_select_constraint_layout)
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

            // 하트 이미지 버튼 없애기

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