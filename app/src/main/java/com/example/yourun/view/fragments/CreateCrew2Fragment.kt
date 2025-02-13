package com.example.yourun.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yourun.R
import com.example.yourun.databinding.FragmentCreateCrew2Binding


class CreateCrew2Fragment : Fragment(R.layout.fragment_create_crew2) {

    private var _binding: FragmentCreateCrew2Binding? = null
    private val binding get() = _binding!!


    private val tendencyImages = mapOf(
        "스프린터" to R.drawable.img_challenge_sprinter,
        "페이스메이커" to R.drawable.img_facemaker_challenge,
        "트레일러너" to R.drawable.img_challenge_trailrunner
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


        val crewName = arguments?.getString("crewName") ?: ""
        val startDate = arguments?.getString("startDate") ?: ""
        val crewMotto = arguments?.getString("crewMotto") ?: ""
        val endDate = arguments?.getString("endDate") ?: ""
        val challengePeriod = arguments?.getInt("challengePeriod") ?: 0
        val tendency = arguments?.getString("tendency") ?: ""

        val formattedText = "$crewMotto,$crewName 와 \n${startDate} ~ ${endDate}($challengePeriod 일 동안) \n최대 거리 러닝하기!"

        val spannable = SpannableString(formattedText)

        // 색상 적용
        // crewMotto,$crewName 부분 (주황색)
        val crewMottoLength = crewMotto.length
        val crewNameLength = crewName.length
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#F4AA3A")), 0, crewMottoLength + crewNameLength + 1, 0) // ',' 포함

        // startDate ~ endDate 부분 (검은색)
        val startDateIndex = crewMottoLength + crewNameLength + 3
        val endDateIndex = startDateIndex + startDate.length + 3
        spannable.setSpan(ForegroundColorSpan(Color.BLACK), startDateIndex, endDateIndex + endDate.length, 0)

        // challengePeriod 부분 (보라색)
        val challengePeriodIndex = endDateIndex + endDate.length + 4
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#9B4DFF")), challengePeriodIndex, challengePeriodIndex + challengePeriod.toString().length, 0)

        // "최대 거리 러닝하기!" 부분 (검은색)
        val runningTextIndex = challengePeriodIndex + challengePeriod.toString().length + 6
        spannable.setSpan(ForegroundColorSpan(Color.BLACK), runningTextIndex, formattedText.length, 0)


        binding.resultSubTitle.text = spannable


        val imageResId = tendencyImages[tendency] ?: R.drawable.img_mate_sprinter
        binding.resultCharacter.setImageResource(imageResId)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}