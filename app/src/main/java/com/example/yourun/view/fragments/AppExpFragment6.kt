package com.example.yourun.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.yourun.R
import com.example.yourun.databinding.FragmentAppExp6Binding
import com.example.yourun.view.activities.MainActivity

class AppExpFragment6 : Fragment() {

    private var _binding: FragmentAppExp6Binding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppExp6Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // "유런 시작하기" 버튼 클릭 시 MainActivity로 이동
        binding.btnStartYourun.setOnClickListener {

            val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().putBoolean("isAppExpSeen", true).apply()


            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }

        // "뒤로 가기" 버튼 클릭 시 이전 Fragment로 이동
        binding.appExpTopBarWithBackButton.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else {
                requireActivity().finish()
            }
        }

        // "유런과 함께 러닝" 텍스트 색상 변경
        val fullText = binding.txtAppExpRunTogether.text.toString()
        val targetText = "유런과 함께 러닝"
        val spannableString = SpannableString(fullText)
        val startIndex = fullText.indexOf(targetText)
        val endIndex = startIndex + targetText.length

        if (startIndex >= 0) {
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.text_purple)),
                startIndex,
                endIndex,
                SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.txtAppExpRunTogether.text = spannableString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}