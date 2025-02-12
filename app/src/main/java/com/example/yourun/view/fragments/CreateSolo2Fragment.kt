package com.example.yourun.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yourun.R
import com.example.yourun.databinding.FragmentCreateSolo2Binding


class CreateSolo2Fragment  : Fragment(R.layout.fragment_create_solo2) {
    private var _binding: FragmentCreateSolo2Binding? = null
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
        _binding = FragmentCreateSolo2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val startDate = arguments?.getString("startDate") ?: ""
        val endDate = arguments?.getString("endDate") ?: ""
        val challengePeriod = arguments?.getInt("challengePeriod") ?: 0
        val tendency = arguments?.getString("tendency") ?: ""
        val challengeDistanceValue = arguments?.getString("challengeDistanceValue")

        val formattedText = " ${startDate} ~ ${endDate}($challengePeriod 일 간) \n매일 $challengeDistanceValue 러닝하기!"

        val spannable = SpannableString(formattedText)

        // startDate ~ endDate 부분 (검은색)
        val startDateIndex = 1 // 공백을 넘어서
        val endDateIndex = startDateIndex + startDate.length + 3 // " ~ "를 넘어서
        spannable.setSpan(ForegroundColorSpan(Color.BLACK), startDateIndex, endDateIndex + endDate.length, 0)

        // challengePeriod 부분 (보라색)
        val challengePeriodIndex = endDateIndex + endDate.length // "()"를 넘어서
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#9B4DFF")), challengePeriodIndex, challengePeriodIndex + challengePeriod.toString().length + 6, 0)

        // 매일 $challengeDistance 부분 (주황색)
        val distanceIndex = challengePeriodIndex + challengePeriod.toString().length + 6
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#F4AA3A")), distanceIndex, distanceIndex + challengeDistanceValue.toString().length, 0)

        // 러닝하기! 부분 (검은색)
        val runningTextIndex = distanceIndex + challengeDistanceValue.toString().length + 6
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