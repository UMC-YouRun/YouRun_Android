package com.example.yourun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
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


        val nickname = arguments?.getString("nickname", "닉네임") ?: "닉네임"


        view.findViewById<TextView>(R.id.txt_app_exp_welcome).text = "$nickname, ${getString(R.string.app_exp_1)}"


        view.findViewById<Button>(R.id.next_btn).setOnClickListener {
            val nextFragment = AppExpFragment2()
            parentFragmentManager.beginTransaction()
                .replace(R.id.app_exp_fragment_container, nextFragment)
                .addToBackStack(null)
                .commit()
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else {
                requireActivity().finish()
            }
        }
    }
}
