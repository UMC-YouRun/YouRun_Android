package com.example.yourun.view.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.yourun.R
import com.example.yourun.databinding.FragmentAppExpBinding

class AppExpFragment : Fragment(R.layout.fragment_app_exp) {

    private var _binding: FragmentAppExpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppExpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val nickname = sharedPref.getString("nickname", "") ?: "null"

        if (nickname.isNotEmpty()) {
            val welcomeText = binding.txtAppExpWelcome.text.toString()
            val spannable = SpannableStringBuilder(nickname + welcomeText).apply {
                setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.text_purple)),
                    0, nickname.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            binding.txtAppExpWelcome.text = spannable
        }

        binding.nextBtn.setOnClickListener {
            slideImage(R.drawable.img_characters3, binding.imgCharacters) {
                val nextFragment = AppExpFragment2()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.app_exp_fragment_container, nextFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else {
                requireActivity().finish()
            }
        }
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