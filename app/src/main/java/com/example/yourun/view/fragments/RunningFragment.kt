package com.example.yourun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.yourun.R

class RunningFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_running, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topBarView = view.findViewById<View>(R.id.before_running_top_bar)
        val titleTextView = topBarView.findViewById<TextView>(R.id.txt_top_bar)
        titleTextView.text = "러닝 시작하기"

        view.findViewById<View>(R.id.bgd_mate_select).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, RunningMateSelectFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<View>(R.id.running_time_set).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, RunningTimeFragment())
                .addToBackStack(null)
                .commit()
        }

        /* 메이트 데이터를 받으면 메이트 선택 뷰 이미지 변경
           러닝 시간 설정 버튼 활성화
           시작하기 버튼은 모든 데이터가 받아졌을 때 동작
         */

        // 러닝 시간 설정 후 텍스트 변경

    }
}