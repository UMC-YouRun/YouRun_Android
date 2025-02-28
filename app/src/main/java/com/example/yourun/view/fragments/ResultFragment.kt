package com.example.yourun.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.yourun.R
import com.example.yourun.databinding.FragmentResultBinding
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.ResultRepository
import com.example.yourun.view.activities.LoginActivity
import com.example.yourun.view.activities.MainActivity
import com.example.yourun.viewmodel.SignUpViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultFragment : Fragment(R.layout.fragment_result) {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val signUpViewModel: SignUpViewModel by activityViewModels() // ✅ 기존과 동일한 ViewModel 사용

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }
    //가입날짜
    private fun saveSignUpDate() {
        val sharedPref = requireActivity().getSharedPreferences("UserData", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        editor.putString("signup_date", currentDate) // 가입 날짜 저장
        editor.apply()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topBar.txtTopBarWithBackButton.text = "러닝테스트 결과"

        val finalScore = arguments?.getInt("FINAL_SCORE") ?: 0

        val resultType = when (finalScore) {
            in 1..6 -> "스프린터"
            in 6..8 -> "페이스메이커"
            in 8..100 -> "트레일러너"
            else -> "알 수 없음"
        }

        val resultData = ResultRepository.getResultByType(resultType)

        resultData?.let {
            binding.resultTitle.text = "러닝 성향 테스트 결과"
            binding.resultSubTitle.text = "당신은 ${it.userType}!"
            binding.resultCharacter.setImageResource(it.characterImageRes)
            binding.resultDescription.text = it.description

            binding.startRunningButton.setOnClickListener {
                completeSignUp(resultType)
            }
        }
    }

    private fun completeSignUp(resultType: String) {
        val signUpRequest = signUpViewModel.getFinalData().copy(tendency = resultType)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ApiClient.getApiService()
                apiService.signUp(signUpRequest)

                withContext(Dispatchers.Main) {
                    saveSignUpDate()
                    Toast.makeText(requireContext(), "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    saveSignUpDate()
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
