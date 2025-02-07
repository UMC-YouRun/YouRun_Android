package com.example.yourun.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.yourun.R
import com.example.yourun.databinding.FragmentSignup1Binding
import com.example.yourun.model.network.ApiClient
import com.example.yourun.viewmodel.SignUpViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class SignUpFragment1 : Fragment(R.layout.fragment_signup1) {
    private var _binding: FragmentSignup1Binding? = null
    private val binding get() = _binding!!
    private val signUpViewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignup1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPasswordVisibilityToggle()
        setupPasswordCheck()

        binding.topBar.txtTopBarWithBackButton.text = "회원가입"

        binding.btnNext.setOnClickListener {
            val email = binding.etId.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val passwordCheck = binding.editTextPasswordCheck.text.toString().trim()

            if (!isEmailValid(email)) {
                Toast.makeText(requireContext(), "유효한 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isPasswordValid(password)) {
                Toast.makeText(requireContext(), "비밀번호는 영문과 숫자를 포함한 8~10자여야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != passwordCheck) {
                Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signUpViewModel.setEmailAndPassword(email, password, passwordCheck)

            Log.d("SignUpFragment1", "✔️ ViewModel에 저장됨: email=$email, password=$password , passwordCheck=$passwordCheck")

            // ✅ Fragment 간 이동
            findNavController().navigate(R.id.action_signUpFragment1_to_signUpFragment2)
        }

        binding.btnDuplicate.setOnClickListener {
            val email = binding.etId.text.toString().trim()

            if (!isEmailValid(email)) {
                Toast.makeText(requireContext(), "유효한 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            checkEmailDuplicate(email)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"
        return Pattern.compile(pattern).matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        val pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,10}$"
        return Pattern.compile(pattern).matcher(password).matches()
    }

    private fun setupPasswordVisibilityToggle() {
        binding.etPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.etPassword.compoundDrawablesRelative[2]
                if (drawableEnd != null && event.rawX >= (binding.etPassword.right - drawableEnd.bounds.width())) {
                    togglePasswordVisibility(
                        binding.etPassword,
                        R.drawable.ic_visibilityon,
                        R.drawable.ic_visibilityoff
                    )
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.editTextPasswordCheck.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.editTextPasswordCheck.compoundDrawablesRelative[2]
                if (drawableEnd != null && event.rawX >= (binding.editTextPasswordCheck.right - drawableEnd.bounds.width())) {
                    togglePasswordVisibility(
                        binding.editTextPasswordCheck,
                        R.drawable.ic_visibilityon,
                        R.drawable.ic_visibilityoff
                    )
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun setupPasswordCheck() {
        // 비밀번호 유효성 검사
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString().trim()
                if (isPasswordValid(password)) {
                    binding.passwordContent.apply {
                        text = "사용 가능한 비밀번호입니다."
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.purple))
                    }
                } else {
                    binding.passwordContent.apply {
                        text = "조건에 맞지 않는 비밀번호입니다."
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 비밀번호 확인 일치 여부 검사
        binding.editTextPasswordCheck.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = binding.etPassword.text.toString().trim()
                val passwordCheck = s.toString().trim()

                if (password == passwordCheck) {
                    binding.passwordCheckContent.apply {
                        text = "비밀번호 확인 완료"
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.purple))
                    }
                } else {
                    binding.passwordCheckContent.apply {
                        text = "비밀번호가 일치하지 않습니다."
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun togglePasswordVisibility(
        editText: EditText,
        visibilityOnDrawable: Int,
        visibilityOffDrawable: Int
    ) {
        val isVisible =
            editText.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

        if (isVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null,
                ContextCompat.getDrawable(requireContext(), visibilityOffDrawable), null
            )
        } else {
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null,
                ContextCompat.getDrawable(requireContext(), visibilityOnDrawable), null
            )
        }

        editText.setSelection(editText.text?.length ?: 0)
    }

    private fun checkEmailDuplicate(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.getApiService(requireContext()).checkEmailDuplicate(email)

                withContext(Dispatchers.Main) {
                    if (response != null && response.status == 200 && response.data) {
                        // 중복되지 않은 이메일 (사용 가능)
                        binding.tvEmailDuplicate.apply {
                            text = "사용 가능한 이메일입니다."
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.purple))
                        }
                        binding.btnNext.isEnabled = true
                    } else {
                        // 이미 존재하는 이메일 (사용 불가능)
                        binding.tvEmailDuplicate.apply {
                            text = "이미 사용 중인 이메일입니다."
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                        }
                        binding.btnNext.isEnabled = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "에러가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

