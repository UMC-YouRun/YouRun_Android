package com.example.yourun.view.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.yourun.R
import com.example.yourun.databinding.FragmentRunningTimeBinding
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.RunningRepository
import com.example.yourun.view.activities.CalendarActivity
import com.example.yourun.viewmodel.RunningViewModel
import com.example.yourun.viewmodel.RunningViewModelFactory

class RunningTimeFragment : Fragment() {

    private var _binding: FragmentRunningTimeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RunningViewModel by viewModels {
        RunningViewModelFactory(RunningRepository(ApiClient.getApiService()))
    }

    private var selectedView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRunningTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val mateId = arguments?.getLong("mateId")
        if (mateId != null) {
            viewModel.fetchMateRunningData(mateId)
        }
        viewModel.mateId.value = mateId
        val mateName = arguments?.getString("mateName")
        viewModel.mateName.value = mateName

        // 상단 바 설정
        binding.runningTimeSetTopBar.txtTopBarWithBackButton.text = "러닝 시작하기"
        binding.runningTimeSetTopBar.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnCalendar.setOnClickListener {
            val intent = Intent(requireContext(), CalendarActivity::class.java)
            startActivity(intent)
        }

        viewModel.mateId.observe(viewLifecycleOwner) { id ->
            viewModel.fetchMateRunningData(id)
        }

        viewModel.mateRunningData.observe(viewLifecycleOwner) { runningData ->
            Log.d("mateRunningData", runningData.toString())
            val mateRunningDistance = runningData?.data?.totalDistance
            val mateRunningPace = runningData?.data?.pace
            viewModel.mateRunningDistance.value = mateRunningDistance
            viewModel.mateRunningPace.value = mateRunningPace

            val nickname = viewModel.mateName.value ?: "메이트"
            val runningTimeMinutes = runningData?.data?.totalTime?.let { it / 60 } ?: 0

            // 닉네임과 러닝 시간 Bold 처리
            val fullText = "러닝 메이트 ${nickname}는 ${runningTimeMinutes}분을 뛰었어요."
            val spannable = SpannableStringBuilder(fullText)

            // 닉네임을 Bold로 설정
            val nicknameStart = fullText.indexOf(nickname)
            val nicknameEnd = nicknameStart + nickname.length
            spannable.setSpan(
                StyleSpan(R.font.roboto_bold),
                nicknameStart,
                nicknameEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // 러닝 시간을 Bold로 설정
            val timeStart = fullText.indexOf(runningTimeMinutes.toString())
            val timeEnd = timeStart + runningTimeMinutes.toString().length
            spannable.setSpan(
                StyleSpan(R.font.roboto_bold),
                timeStart,
                timeEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding.txtRunningTimeMate.text = spannable
        }

        // 시간 선택 버튼들
        val timeButtons = listOf(
            binding.bgdTime15, binding.bgdTime30, binding.bgdTime45, binding.bgdTime60
        )

        // 색상 값 가져오기
        val defaultColor = Color.TRANSPARENT
        val selectedColor = ContextCompat.getColor(requireContext(), R.color.btn_selected)

        // 초기 selectedView 설정
        selectedView = null

        timeButtons.forEach { button ->
            button.setOnClickListener {
                // 이전 선택 해제
                selectedView?.let { prevButton ->
                    getGradientDrawable(prevButton)?.setColor(defaultColor)
                }

                // 새로운 선택 적용
                getGradientDrawable(button)?.setColor(selectedColor)

                selectedView = button
            }
        }

        // 선택한 시간 데이터를 viewModel로 전달
        binding.btnSelectTime.setOnClickListener {
            val targetTime = when (selectedView?.id) {
                binding.bgdTime15.id -> 15
                binding.bgdTime30.id -> 30
                binding.bgdTime45.id -> 45
                binding.bgdTime60.id -> 60
                else ->  15 // 기본값
            }

            // 데이터 저장 후 이전 Fragment로 이동
            val bundle = Bundle().apply {
                putInt("targetTime", targetTime)
                putInt("mateRunningDistance", viewModel.mateRunningDistance.value ?: 0)
                putInt("mateRunningPace", viewModel.mateRunningPace.value ?: 0)
            }

            parentFragmentManager.setFragmentResult("mateRunningDataKey", bundle)
            parentFragmentManager.popBackStack()
        }
    }

    private fun getGradientDrawable(view: View): GradientDrawable? {
        val background = view.background
        return if (background is StateListDrawable) {
            // StateListDrawable에서 GradientDrawable을 추출
            val drawable = background.getCurrent()
            if (drawable is GradientDrawable) drawable else null
        } else if (background is GradientDrawable) {
            background
        } else {
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}