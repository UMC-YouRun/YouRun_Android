package com.example.yourun.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

    private var selectedMateNickname: String? = null
    private var selectedMateTendency: String? = null
    private var selectedMateId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRunningMateSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.viewModel = viewModel
//        binding.lifecycleOwner = viewLifecycleOwner
//
//        viewModel.fetchMateList() // 메이트 목록 데이터 가져오기
//        viewModel.fetchRecommendMates() // 추천 메이트 데이터 가져오기
//
//        binding.mateSelectTopBar.txtTopBarWithBackButton.text = "러닝메이트 선택"
//
//        binding.btnCalendar.setOnClickListener {
//            val intent = Intent(requireContext(), CalendarActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.mateSelectTopBar.backButton.setOnClickListener {
//            navigateBackWithSelection()
//        }
//
//        // 메이트 목록 UI 업데이트
//        viewModel.mateList.observe(viewLifecycleOwner) { mates ->
//            updateMatesUI(mates, viewModel, isRecommended = false)
//        }
//
//        // 추천 메이트 UI 업데이트
//        viewModel.recommendMates.observe(viewLifecycleOwner) { mates ->
//            updateMatesUI(mates, viewModel, isRecommended = true)
//        }
//    }

    // 메이트 UI 업데이트 (추천 메이트 & 일반 메이트 목록 통합)
    private fun updateMatesUI(
        mates: List<UserMateInfo>,
        viewModel: RunningViewModel,
        isRecommended: Boolean
    ) {
        // 추천 메이트는 recommendMatesContainer에, 일반 메이트 목록은 matesListContainer에 추가
        val container = if (isRecommended) binding.recommendMatesContainer else binding.matesListContainer

        // 기존 메이트 뷰 삭제
        container.removeAllViews()

        // 메이트 목록 추가 (최대 5개)
        mates.take(5).forEachIndexed { index, mate ->
            // 가로 구분선 추가 (첫 번째 항목 제외)
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

            // CustomMateView 추가
            val mateView = CustomMateView<RunningViewModel>(requireContext(), showHeartButton = false).apply {
                setViewModel(viewModel)
                updateMateInfo(mate, index + 1)
                onMateSelected = { nickname, tendency, id ->
                    selectedMateNickname = nickname
                    selectedMateTendency = tendency
                    selectedMateId = id
                }

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

    private fun navigateBackWithSelection() {
        val bundle = Bundle().apply {
            putString("mateName", selectedMateNickname)
            putString("mateTendency", selectedMateTendency)
            selectedMateId?.let { putLong("mateId", it) }
        }
        parentFragmentManager.setFragmentResult("mateSelectKey", bundle)

        parentFragmentManager.popBackStack() // RunningFragment로 돌아가기
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}