package com.example.yourun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.yourun.R

class ChallengeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트의 레이아웃을 인플레이트
        val view = inflater.inflate(R.layout.activity_running_challenge, container, false)

        // 상단바 제목 변경
        val topBarTitle: TextView = view.findViewById(R.id.txt_top_bar_with_back_button)
        topBarTitle.text = "러닝 챌린지"

        return view
    }
}
