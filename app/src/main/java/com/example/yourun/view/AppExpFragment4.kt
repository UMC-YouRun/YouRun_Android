package com.example.yourun.view

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.yourun.R

class AppExpFragment4: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_exp4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.next_btn).setOnClickListener {
            val nextFragment = AppExpFragment5()
            parentFragmentManager.beginTransaction()
                .replace(R.id.app_exp_fragment_container, nextFragment)
                .addToBackStack(null)
                .commit()
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

        val txt_who_run = view.findViewById<TextView>(R.id.txt_app_exp_who_run)

        val fullText = txt_who_run.text.toString()
        val targetText = "더 많이, 더 꾸준히"
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
        txt_who_run.text = spannableString

    }
}