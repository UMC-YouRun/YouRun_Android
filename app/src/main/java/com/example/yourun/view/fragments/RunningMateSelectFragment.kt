package com.example.yourun.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.yourun.R
import com.example.yourun.databinding.FragmentRunningMateSelectBinding
import com.example.yourun.model.data.response.UserMateInfo
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.RunningRepository
import com.example.yourun.view.activities.CalendarActivity
import com.example.yourun.view.custom.CustomMateView
import com.example.yourun.viewmodel.RunningViewModel
import com.example.yourun.viewmodel.RunningViewModelFactory

class RunningMateSelectFragment : Fragment() {

    private var _binding: FragmentRunningMateSelectBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RunningViewModel by viewModels {
        RunningViewModelFactory(RunningRepository(ApiClient.getApiService()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRunningMateSelectBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mateSelectTopBar.txtTopBarWithBackButton.text = "러닝메이트 선택"

        binding.btnCalendar.setOnClickListener {
            val intent = Intent(requireContext(), CalendarActivity::class.java)
            startActivity(intent)
        }

        binding.mateSelectTopBar.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 추천 메이트 UI 업데이트
        viewModel.recommendMates.observe(viewLifecycleOwner) { mates ->
            updateRecommendMatesUI(mates, viewModel)
        }
    }

    // 추천 메이트 UI 추가
    private fun <T: ViewModel> updateRecommendMatesUI(mates: List<UserMateInfo>, viewModel: T) {
        val container = binding.recommendMatesContainer

        // 기존 추천 메이트 뷰 삭제
        container.removeAllViews()

        // 추천 메이트 목록 추가 (최대 5개)
        mates.take(5).forEachIndexed { index, mate ->
            // 가로 경계선 추가 (첫 번째 뷰 제외)
            if (index > 0) {
                val dividerView = View(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.dpToPx()
                    ).apply {
                        setMargins(24.dpToPx(), 0, 24.dpToPx(), 4.dpToPx())
                    }
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.border))
                    tag = "divider" // 제거할 때 구분하기 위해 태그 추가
                }
                container.addView(dividerView)
            }

            // 추천 메이트 CustomMateView 추가
            val mateView = CustomMateView<T>(requireContext()).apply {
                setViewModel(viewModel)
                updateMateInfo(mate, mates.size - index)

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(24.dpToPx(), 8.dpToPx(), 24.dpToPx(), 8.dpToPx())
                }
            }

            container.addView(mateView)
        }
    }

    // dp → px 변환 함수
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}