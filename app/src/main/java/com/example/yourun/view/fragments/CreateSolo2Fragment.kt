package com.example.yourun.view.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yourun.R
import com.example.yourun.databinding.FragmentCreateSolo2Binding
import com.example.yourun.view.activities.MainActivity


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

        val formattedText = " ${startDate} ~ \n${endDate}($challengePeriod 일 간) \n매일 $challengeDistanceValue 러닝하기!"
        val spannable = SpannableString(formattedText)

// "startDate ~ endDate" 부분 (검은색)
        val startDateToEndDate = "${startDate} ~ ${endDate}"
        val startDateIndex = formattedText.indexOf(startDateToEndDate)
        if (startDateIndex != -1) {
            spannable.setSpan(
                ForegroundColorSpan(Color.BLACK),
                startDateIndex,
                startDateIndex + startDateToEndDate.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

// "challengePeriod" 부분 (보라색)
        val challengePeriodText = "($challengePeriod 일 간)"
        val challengePeriodIndex = formattedText.indexOf(challengePeriodText)
        if (challengePeriodIndex != -1) {
            spannable.setSpan(
                ForegroundColorSpan(Color.parseColor("#9B4DFF")),
                challengePeriodIndex,
                challengePeriodIndex + challengePeriodText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

// "매일" 부분 (검은색)
        val dailyIndex = formattedText.indexOf("매일")
        if (dailyIndex != -1) {
            spannable.setSpan(
                ForegroundColorSpan(Color.BLACK),
                dailyIndex,
                dailyIndex + "매일".length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

// "challengeDistanceValue" 부분 (주황색)
        val challengeDistanceText = "$challengeDistanceValue 러닝하기!"
        val distanceIndex = formattedText.indexOf(challengeDistanceText)
        if (distanceIndex != -1) {
            spannable.setSpan(
                ForegroundColorSpan(Color.parseColor("#F4AA3A")),
                distanceIndex,
                distanceIndex + challengeDistanceText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

// "러닝하기!" 부분 (검은색)
        val runningTextIndex = formattedText.indexOf("러닝하기!")
        if (runningTextIndex != -1) {
            spannable.setSpan(
                ForegroundColorSpan(Color.BLACK),
                runningTextIndex,
                formattedText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        binding.resultSubTitle.text = spannable





        val imageResId = tendencyImages[tendency] ?: R.drawable.img_mate_sprinter
        binding.resultCharacter.setImageResource(imageResId)

        binding.startRunningButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }



    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}