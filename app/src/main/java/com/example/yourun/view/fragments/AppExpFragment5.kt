package com.example.yourun.view.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.yourun.R
import com.example.yourun.databinding.FragmentAppExp5Binding

class AppExpFragment5 : Fragment() {

    private var _binding: FragmentAppExp5Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppExp5Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // "다음" 버튼 클릭 시 다음 Fragment로 이동
        binding.nextBtn.setOnClickListener {
            slideImage(R.drawable.img_characters, binding.imgCharacters) {
                val nextFragment = AppExpFragment6()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.app_exp_fragment_container, nextFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        // "뒤로 가기" 버튼 클릭 시 이전 Fragment로 이동
        binding.appExpTopBarWithBackButton.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // android back button
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else {
                requireActivity().finish()
            }
        }

        // "얼마나 러닝" 텍스트 색상 변경
        val fullText = binding.txtAppExpHowRun.text.toString()
        val targetText = "얼마나 러닝"
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
        binding.txtAppExpHowRun.text = spannableString
    }

    // 이미지 변경 + 애니메이션 (왼쪽으로 슬라이드)
    private fun slideImage(newImageRes: Int, imageView: ImageView, onAnimationEnd: () -> Unit) {
        val animOut = ObjectAnimator.ofFloat(imageView, "translationX", 0f, -imageView.width.toFloat())
        animOut.duration = 300

        val animIn = ObjectAnimator.ofFloat(imageView, "translationX", imageView.width.toFloat(), 0f)
        animIn.duration = 300

        animOut.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                imageView.setImageResource(newImageRes) // 이미지 변경
                animIn.start()
                animIn.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        onAnimationEnd() // 애니메이션 끝나면 Fragment 변경
                    }
                })
            }
        })

        animOut.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}