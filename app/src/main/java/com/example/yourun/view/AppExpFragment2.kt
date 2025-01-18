package com.example.yourun.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.example.yourun.R

class AppExpFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_exp2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* view.findViewById<Button>(R.id.next_btn).setOnClickListener {
            val nextFragment = AppExpFragment3()
            parentFragmentManager.beginTransaction()
                .replace(R.id.app_exp_fragment_container, nextFragment)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit()
        } */

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }

    }
}