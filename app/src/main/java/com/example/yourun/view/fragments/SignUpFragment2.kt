package com.example.yourun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.yourun.R
import com.example.yourun.databinding.FragmentSignup2Binding
import com.example.yourun.viewmodel.SignUpViewModel

class SignUpFragment2 : Fragment(R.layout.fragment_signup2) {
    private var _binding: FragmentSignup2Binding? = null
    private val binding get() = _binding!!
    private val signUpViewModel: SignUpViewModel by activityViewModels() // ✅ ViewModel 가져오기

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignup2Binding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topBar.txtTopBarWithBackButton.text = "회원가입"

        binding.myCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.btnNext.isEnabled = isChecked
        }

        binding.btnNext.setOnClickListener {
            if (binding.myCheckBox.isChecked) {
                findNavController().navigate(R.id.action_signUpFragment2_to_signUpFragment3)
            } else {
                Toast.makeText(requireContext(), "약관 동의가 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
