package com.example.yourun.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yourun.R
import com.example.yourun.databinding.FragmentCreateCrew2Binding

class CreateCrew2Fragment : Fragment(R.layout.fragment_create_crew2) {

    private var _binding: FragmentCreateCrew2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateCrew2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Your fragment code here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
