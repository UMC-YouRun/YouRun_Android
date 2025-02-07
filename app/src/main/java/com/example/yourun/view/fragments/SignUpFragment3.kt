package com.example.yourun.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.yourun.R
import com.example.yourun.databinding.FragmentSignup3Binding
import com.example.yourun.viewmodel.SignUpViewModel

class SignUpFragment3 : Fragment(R.layout.fragment_signup3) {
    private var _binding: FragmentSignup3Binding? = null
    private val binding get() = _binding!!
    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private var selectedTags = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignup3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topBar.txtTopBarWithBackButton.text = "회원가입"

        setCheckBoxListener(binding.checkbox1, "느긋하게")
        setCheckBoxListener(binding.checkbox2, "음악과")
        setCheckBoxListener(binding.checkbox3, "열정적")
        setCheckBoxListener(binding.checkbox4, "자기계발")
        setCheckBoxListener(binding.checkbox5, "에너자이저")
        setCheckBoxListener(binding.checkbox6, "왕초보")

        binding.imgBtnSignupSuccess.setOnClickListener {
            val nickname = binding.editTextNickname.text.toString()

            if (isValidNickname(nickname) && selectedTags.size == 2) {
                signUpViewModel.setNickname(nickname)
                signUpViewModel.setTags(selectedTags[0], selectedTags[1])

                Log.d("SignUpFragment3", "✅ 닉네임 & 태그 저장됨: nickname=$nickname, tag1=${selectedTags[0]}, tag2=${selectedTags[1]}")

                // ✅ 기존 Activity 이동 코드 삭제하고 Navigation으로 Fragment 이동
                findNavController().navigate(R.id.action_signUpFragment3_to_questionFragment)

            } else {
                Toast.makeText(requireContext(), "닉네임은 한글 2~4자여야 하며, 태그 2개를 선택해야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun isValidNickname(nickname: String): Boolean {
        val regex = "^[가-힣]{2,4}$".toRegex()
        return regex.matches(nickname)
    }

    private fun setCheckBoxListener(checkBox: CheckBox, tag: String) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (selectedTags.size < 2) {
                    selectedTags.add(tag)
                    Log.d("SignUpFragment3", "✔️ 태그 선택됨: $tag")
                } else {
                    Toast.makeText(requireContext(), "2개만 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    checkBox.isChecked = false
                }
            } else {
                selectedTags.remove(tag)
                Log.d("SignUpFragment3", "❌ 태그 제거됨: $tag")
            }

            if (selectedTags.size == 1) {
                //Toast.makeText(requireContext(), "2개 선택 필수입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
