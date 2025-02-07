package com.example.yourun.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.yourun.R
import com.example.yourun.databinding.FragmentResultBinding
import com.example.yourun.model.network.ApiClientNoAuth
import com.example.yourun.model.repository.ResultRepository
import com.example.yourun.view.activities.MainActivity
import com.example.yourun.viewmodel.SignUpViewModel
import com.google.gson.Gson
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topBar.txtTopBarWithBackButton.text = "러닝테스트 결과"

        val finalScore = arguments?.getInt("FINAL_SCORE") ?: 0

        val resultType = when (finalScore) {
            in 3..4 -> "스프린터"
            in 5..6 -> "페이스메이커"
            in 7..8 -> "마라토너"
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
        Log.d("ResultFragment", "🔍 completeSignUp 호출됨! resultType: $resultType")

        val signUpRequest = signUpViewModel.getFinalData().copy(tendency = resultType)

        Log.d("ResultFragment", "📦 최종 회원가입 데이터: $signUpRequest")
        Log.d("ResultFragment", "📦 최종 요청 JSON: ${Gson().toJson(signUpRequest)}")


        /*
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ApiClientNoAuth.getApiService() // ✅ 인증 없이 회원가입 요청
                val response = apiService.signUp(signUpRequest)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "회원가입 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("ResultFragment", "❌ 회원가입 실패: ${e.message}")
                }
            }
        }
         */

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
