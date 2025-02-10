package com.example.yourun.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.yourun.R
import com.example.yourun.databinding.FragmentSignup3Binding
import com.example.yourun.model.network.ApiClient
import com.example.yourun.viewmodel.SignUpViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignUpFragment3 : Fragment(R.layout.fragment_signup3) {
    private var _binding: FragmentSignup3Binding? = null
    private val binding get() = _binding!!
    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private var selectedTags = mutableListOf<String>()
    private var selectedCount = 0

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

        // 체크박스 리스너 설정
        setCheckBoxListener(binding.checkbox1, "느긋하게")
        setCheckBoxListener(binding.checkbox2, "음악과")
        setCheckBoxListener(binding.checkbox3, "열정적")
        setCheckBoxListener(binding.checkbox4, "자기계발")
        setCheckBoxListener(binding.checkbox5, "에너자이저")
        setCheckBoxListener(binding.checkbox6, "왕초보")

        // 닉네임 입력 감지 및 중복 확인
        binding.editTextNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val nickname = editable.toString().trim()
                if (isValidNickname(nickname)) {
                    checkNicknameDuplicate(nickname) // 닉네임 중복 확인
                } else {
                    binding.nicknameContent.apply {
                        text = "닉네임은 한글 2~4자만 입력 가능합니다."
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    }
                    binding.imgBtnSignupSuccess.isEnabled = false
                }
            }
        })

        // 회원가입 완료 버튼
        binding.imgBtnSignupSuccess.setOnClickListener {
            val nickname = binding.editTextNickname.text.toString()

            if (isValidNickname(nickname) && selectedTags.size == 2) {
                signUpViewModel.setNickname(nickname)
                signUpViewModel.setTags(selectedTags[0], selectedTags[1])

                Log.d(
                    "SignUpFragment3",
                    "✅ 닉네임 & 태그 저장됨: nickname=$nickname, tag1=${selectedTags[0]}, tag2=${selectedTags[1]}"
                )

                val sharedPref = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                // 현재 날짜 (가입 날짜) 저장
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                editor.putString("nickname", nickname) // 닉네임 저장
                editor.putString("signup_date", currentDate) // 가입 날짜 저장
                editor.apply() // 비동기 저장

                // Navigation으로 다른 Fragment로 이동
                findNavController().navigate(R.id.action_signUpFragment3_to_questionFragment)

            } else {
                Toast.makeText(
                    requireContext(),
                    "닉네임은 한글 2~4자여야 하며, 태그 2개를 선택해야 합니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    // 닉네임 유효성 검사 (한글 2~4자)
    private fun isValidNickname(nickname: String): Boolean {
        val regex = "^[가-힣]{2,4}$".toRegex()
        return regex.matches(nickname)
    }

    // 닉네임 중복 확인
    private fun checkNicknameDuplicate(nickname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.getApiService().checkNicknameDuplicate(nickname)

                // 응답 확인 로그
                Log.d("API Response", "Response: $response")

                withContext(Dispatchers.Main) {
                    if (response != null && response.status == 200 && response.data) {
                        // 사용 가능한 닉네임
                        binding.nicknameContent.apply {
                            text = "사용 가능한 닉네임입니다."
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.purple))
                        }
                        binding.imgBtnSignupSuccess.isEnabled = true
                    } else {
                        // 중복된 닉네임
                        binding.nicknameContent.apply {
                            text = "이미 사용 중인 닉네임입니다."
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                        }
                        binding.imgBtnSignupSuccess.isEnabled = false
                    }
                }
            } catch (e: Exception) {
                // 예외 처리
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "에러가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 체크박스 리스너
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
