package com.example.yourun.view.fragments

import android.content.Intent
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
import com.example.yourun.view.activities.MainActivity

class AppExpFragment6 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_exp6, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_start_yourun).setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
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

        val txtRunTogether = view.findViewById<TextView>(R.id.txt_app_exp_run_together)

        val fullText = txtRunTogether.text.toString()
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
        txtRunTogether.text = spannableString
    }
}