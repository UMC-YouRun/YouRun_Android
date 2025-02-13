package com.example.yourun.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yourun.databinding.FragmentMyrunBinding
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.MyRunRepository
import com.example.yourun.viewmodel.MyRunViewModel
import com.example.yourun.viewmodel.MyRunViewModelFactory


import android.util.Log
import com.example.yourun.R
import com.example.yourun.view.activities.MyRun2Activity

class MyRunFragment : Fragment(R.layout.fragment_myrun) {
    private var _binding: FragmentMyrunBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MyRunViewModel

    private val tendencyImages = mapOf(
        "페이스메이커" to listOf(R.drawable.img_pace1, R.drawable.img_pace2, R.drawable.img_pace3, R.drawable.img_pace4),
       "트레일러너" to listOf(R.drawable.img_trail1, R.drawable.img_trail2, R.drawable.img_trail3, R.drawable.img_trail4),
       "스프린터" to listOf(R.drawable.img_sprinter1, R.drawable.img_sprinter2, R.drawable.img_sprinter3, R.drawable.img_sprinter4)
    )

    private var selectedImageRes : Int = R.drawable.img_pace1




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyrunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = MyRunRepository(ApiClient.getApiService())
        val factory = MyRunViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MyRunViewModel::class.java]

        Log.d("MyRunFragment", "ViewModel 초기화 완료") // ViewModel 생성 확인

        viewModel.fetchMyRunData()
        Log.d("MyRunFragment", "fetchMyRunData() 호출") // 데이터 요청 확인

        viewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            Log.d("MyRunFragment", "LiveData 변경 감지: $userInfo") // 데이터 변경 감지

            userInfo?.let {
                binding.tvMyrunName.text = it.nickname
                binding.tvMyrunTag.text = it.tags.joinToString(", ")
                binding.txtCrewReward1.text = it.crewReward.toString()
                binding.txtSoloReward.text = it.personalReward.toString()
                binding.txtMvp.text = it.mvp.toString()

                val imageRes = getRandomImageForTendency(it.tendency, requireContext())
                binding.imgProfile.setImageResource(imageRes)



                Log.d("MyRunFragment", "UI 업데이트 완료") // UI 업데이트 확인
            } ?: Log.e("MyRunFragment", "userInfo가 null입니다.") // 데이터가 없을 때
        }

        binding.btnEdit.setOnClickListener {
            viewModel.userInfo.value?.let { userInfo ->
                val intent = Intent(requireActivity(), MyRun2Activity::class.java)
                val imageRes = getRandomImageForTendency(userInfo.tendency, requireContext())

                Log.d("MyRunFragment", "전달할 이미지 리소스: $imageRes") // 로그 확인

                intent.putExtra("profile_image_res", imageRes)
                startActivity(intent)
            } ?: Log.e("MyRunFragment", "userInfo가 null이라 이미지 전달 불가")
        }
    }

    private fun getRandomImageForTendency(tendency: String, context: Context): Int {
        val sharedPref = context.getSharedPreferences("MyRunPrefs", Context.MODE_PRIVATE)
        val savedImage = sharedPref.getInt("tendency_image_$tendency", -1)

        return if (savedImage != -1) {
            savedImage
        } else {
            val images = tendencyImages[tendency] ?: emptyList()
            val randomImage = images.randomOrNull() ?: R.drawable.img_pace1 // 기본 이미지 설정
            sharedPref.edit().putInt("tendency_image_$tendency", randomImage).apply()
            randomImage
        }
    }



    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
            Log.d("MyRunFragment", "onDestroyView() 호출됨") // Fragment 소멸 확인
        }
    }
