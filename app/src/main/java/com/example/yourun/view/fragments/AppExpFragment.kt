package com.example.yourun.view.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.yourun.R

class AppExpFragment : Fragment(R.layout.fragment_app_exp) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_exp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nickname = arguments?.getString("nickname", null) ?: ""
     
        val welcomeTextView = view.findViewById<TextView>(R.id.txt_app_exp_welcome)

        if (nickname.isNotEmpty()) {
        val spannable = SpannableStringBuilder(nickname).apply {
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.text_purple)),
                0, length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        spannable.append(welcomeTextView.text) // 기존 텍스트 추가
        welcomeTextView.text = spannable
        }

        val imgCharactersView = view.findViewById<ImageView>(R.id.img_characters)

        view.findViewById<Button>(R.id.next_btn).setOnClickListener {
            slideImage(R.drawable.img_characters3, imgCharactersView) {
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
}