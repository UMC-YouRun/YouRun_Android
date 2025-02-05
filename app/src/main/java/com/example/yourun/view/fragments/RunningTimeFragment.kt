package com.example.yourun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.yourun.R

class RunningTimeFragment : Fragment() {

    private lateinit var selectedView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_running_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topBarView = view.findViewById<View>(R.id.running_time_set_top_bar)
        val titleTextView = topBarView.findViewById<TextView>(R.id.txt_top_bar_with_back_button)
        titleTextView.text = "러닝 시작하기"

        view.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val timeButton15 = view.findViewById<View>(R.id.bgd_time_15)
        val timeButton30 = view.findViewById<View>(R.id.bgd_time_30)
        val timeButton45 = view.findViewById<View>(R.id.bgd_time_45)
        val timeButton60 = view.findViewById<View>(R.id.bgd_time_60)

        val views = listOf(timeButton15, timeButton30, timeButton45, timeButton60)

        views.forEach { view ->
            view.setOnClickListener {
                selectedView.isSelected = false // 이전 선택 해제
                view.isSelected = true // 현재 선택 설정
                selectedView = view // 선택한 View 저장
            }
        }

        var targetTime = "15분" // default 값
        val selectButton = view.findViewById<View>(R.id.btn_select_time)
        selectButton.setOnClickListener {
            when (selectedView.id) {
                R.id.bgd_time_15 -> {
                    targetTime = "15분"
                }
                R.id.bgd_time_30 -> {
                    targetTime = "30분"
                }
                R.id.bgd_time_45 -> {
                    targetTime = "45분"
                }
                R.id.bgd_time_60 -> {
                    targetTime = "60분"
                }
            }
            // 사용자가 선택한 시간 데이터 전달
            val bundle = Bundle().apply {
                putString("target_time", targetTime)
            }
            parentFragmentManager.setFragmentResult("requestKey", bundle)
        }

    }
}