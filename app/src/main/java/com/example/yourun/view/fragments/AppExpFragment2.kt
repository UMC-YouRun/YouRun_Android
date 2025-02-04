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
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.yourun.R

class AppExpFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_exp2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imgCharactersView = view.findViewById<ImageView>(R.id.img_characters)

        view.findViewById<Button>(R.id.next_btn).setOnClickListener {
            slideImage(R.drawable.img_characters, imgCharactersView) {
                val nextFragment = AppExpFragment3()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.app_exp_fragment_container, nextFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        view.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
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

        val txtWhatCharacter = view.findViewById<TextView>(R.id.txt_app_exp_what_character)

        val fullText = txtWhatCharacter.text.toString()
        val targetText = "캐릭터"
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
        txtWhatCharacter.text = spannableString

    }

    // 🔹 이미지 변경 + 애니메이션 (왼쪽으로 슬라이드)
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