package com.example.yourun.view.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yourun.R
import com.example.yourun.databinding.FragmentCreateCrew2Binding
import com.example.yourun.view.activities.MainActivity


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

        val formattedText = "$crewMotto,$crewName 와 \n${startDate} ~ \n${endDate}($challengePeriod 일 동안) \n최대 거리 러닝하기!"

        val spannable = SpannableString(formattedText)

    // 색상 적용
    // crewMotto,$crewName 부분 (주황색)
        val crewMottoIndex = formattedText.indexOf(crewMotto)
        val crewNameIndex = formattedText.indexOf(crewName)
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#F4AA3A")),
            crewMottoIndex,
            crewNameIndex + crewName.length + 1, // ',' 포함
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

    // "와" 부분 (검은색)
        val waIndex = formattedText.indexOf("와")
        if (waIndex != -1) {
            spannable.setSpan(
                ForegroundColorSpan(Color.BLACK),
                waIndex,
                waIndex + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

    // startDate ~ endDate 부분 (검은색)
        val startDateIndex = formattedText.indexOf(startDate)
        val endDateIndex = formattedText.indexOf(endDate)
        if (startDateIndex != -1 && endDateIndex != -1) {
            spannable.setSpan(
                ForegroundColorSpan(Color.BLACK),
                startDateIndex,
                endDateIndex + endDate.length + 3, // " ~ "를 포함
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val challengePeriodText = "(${challengePeriod} 일 동안)"
        val challengePeriodIndex = formattedText.indexOf(challengePeriodText)
        if (challengePeriodIndex != -1) {
            spannable.setSpan(
                ForegroundColorSpan(Color.parseColor("#9B4DFF")),
                challengePeriodIndex,
                challengePeriodIndex + challengePeriodText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

    // "최대 거리 러닝하기!" 부분 (검은색)
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