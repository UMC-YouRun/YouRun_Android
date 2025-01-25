package com.example.yourun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.yourun.R

class RunningFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_running, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topBarView = view.findViewById<View>(R.id.running_top_bar)
        val titleTextView = topBarView.findViewById<TextView>(R.id.txt_top_bar)
        titleTextView.text = "러닝 시작하기"

        view.findViewById<ImageButton>(R.id.btn_mate_select).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.running_fragment_container, RunningMateSelectFragment())
                .addToBackStack(null)
                .commit()
        }

    }
}