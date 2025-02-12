package com.example.yourun.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yourun.R
import com.example.yourun.databinding.FragmentCreateCrew2Binding

class CreateCrew2Fragment : Fragment(R.layout.fragment_create_crew2) {

    private var _binding: FragmentCreateCrew2Binding? = null
    private val binding get() = _binding!!

    // 이미지 리소스 설정
    private val tendencyImages = mapOf(
        "스프린터" to R.drawable.img_mate_sprinter,
        "페이스메이커" to R.drawable.img_crew_facemaker,
        "트레일러너" to R.drawable.img_crew_trailrunner
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateCrew2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 이전 프래그먼트에서 전달된 데이터 가져오기
        val crewName = arguments?.getString("crewName") ?: ""
        val startDate = arguments?.getString("startDate") ?: ""
        val crewMotto = arguments?.getString("crewMotto") ?: ""
        val endDate = arguments?.getString("endDate") ?: ""
        val challengePeriod = arguments?.getInt("challengePeriod") ?: 0
        val tendency = arguments?.getString("tendency") ?: ""

        // resultSubTitle 텍스트 변경
        val formattedText = "$crewMotto,$crewName 와 \n${startDate} ~ ${endDate}($challengePeriod 일 동안) \n최대러닝하기!"
        binding.resultSubTitle.text = formattedText

        // tendency에 맞는 이미지 설정
        val imageResId = tendencyImages[tendency] ?: R.drawable.img_mate_sprinter // 기본 이미지 설정
        binding.resultCharacter.setImageResource(imageResId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}