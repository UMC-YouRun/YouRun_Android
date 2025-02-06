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
import org.json.JSONObject
import org.json.JSONTokener

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
            in 4..6 -> "스프린터"
            in 6..7 -> "페이스메이커"
            in 7..8 -> "트레일러너"
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
                val apiService = ApiClientNoAuth.getApiService()
                apiService.signUp(signUpRequest)

                withContext(Dispatchers.Main) {
//                    Log.d("ResultFragment", "✅ 회원가입 요청 완료. 응답 체크 없이 MainActivity로 이동.")

                    Toast.makeText(requireContext(), "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
//                    Log.e("ResultFragment", "❌ 회원가입 요청 중 오류 발생: ${e.message}")

                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
    }

//    private fun completeSignUp(resultType: String) {
//        Log.d("ResultFragment", "🔍 completeSignUp 호출됨! resultType: $resultType")
//
//        val signUpRequest = signUpViewModel.getFinalData().copy(tendency = resultType)
// //
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val apiService = ApiClientNoAuth.getApiService()
//                val response = apiService.signUp(signUpRequest)
//
//                withContext(Dispatchers.Main) {
//                    Log.d("ResultFragment", "📩 Raw Response: ${Gson().toJson(response)}")
//
//                    if (response?.status == 200) {
//                        Log.d("ResultFragment", "✅ 회원가입 성공!")
//
//                        val isSuccess = response.data as? Boolean ?: true
//                        if (isSuccess) {
//                            Toast.makeText(requireContext(), "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(requireActivity(), MainActivity::class.java)
//                            startActivity(intent)
//                            requireActivity().finish()
//                        } else {
//                            Log.e("ResultFragment", "⚠️ 회원가입 실패: 서버에서 data=false 반환")
//                            Toast.makeText(requireContext(), "회원가입 실패: 서버 오류", Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        Log.e("ResultFragment", "❌ 회원가입 실패: 상태 코드 ${response?.status}")
//                        Log.e("ResultFragment", "❌ 오류 메시지: ${response?.message}")
//                        Toast.makeText(requireContext(), "회원가입 실패: ${response?.message}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(requireContext(), "회원가입 실패: ${e.message}", Toast.LENGTH_SHORT).show()
//                    Log.e("ResultFragment", "❌ 회원가입 실패: ${e.message}")
//                }
//            }
//        }
//    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
