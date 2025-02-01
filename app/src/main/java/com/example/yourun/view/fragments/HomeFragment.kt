package com.example.yourun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.yourun.R
import com.example.yourun.databinding.FragmentHomeBinding
import com.example.yourun.model.data.UserCrewChallengeInfo
import com.example.yourun.model.data.UserSoloChallengeInfo
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.HomeChallengeRepository
import com.example.yourun.view.custom.CustomHomeChallenge
import com.example.yourun.viewmodel.HomeViewModel
import com.example.yourun.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(HomeChallengeRepository(ApiClient.getHomeApiService(requireContext())),
            requireActivity().application)
    }

    private var isCrewSelected = true // 초기값 : 크루 챌린지가 선택된 상태

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this  // LiveData 연동
        binding.viewModel = viewModel  // XML에서 ViewModel 사용 가능
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 서버에서 챌린지 데이터 가져오기, 처음 한 번 호출
        viewModel.fetchHomeChallengeData()

        viewModel.isPressedCrew.observe(viewLifecycleOwner) { isPressed ->
            binding.btnCrew.setImageResource(
                if (isPressed) R.drawable.img_crew_btn_selected else R.drawable.img_crew_btn_unselected
            )
        }

        viewModel.isPressedSolo.observe(viewLifecycleOwner) { isPressed ->
            binding.btnSolo.setImageResource(
                if (isPressed) R.drawable.img_solo_btn_selected else R.drawable.img_solo_btn_unselected
            )
        }

        binding.btnCrew.setOnClickListener {
            viewModel.toggleCrewButton()
            isCrewSelected = true
            updateChallengeView()
        }
        binding.btnSolo.setOnClickListener {
            viewModel.toggleSoloButton()
            isCrewSelected = false
            updateChallengeView()
        }

        // ViewModel의 챌린지 데이터 옵저빙
        viewModel.challengeData.observe(viewLifecycleOwner) {
            updateChallengeView() // 데이터 변경될 때 UI 업데이트
        }

        binding.btnAddChallenge.setOnClickListener {
            // 챌린지 추가 화면 이동
        }
    }

    // 버튼 상태에 따라 챌린지 UI 업데이트
    private fun updateChallengeView() {
        val challengeData = viewModel.challengeData.value
        if (isCrewSelected) {
            challengeData?.crewChallenge?.let { replaceButtonWithCustomView(null, it) }
        } else {
            challengeData?.soloChallenge?.let { replaceButtonWithCustomView(it, null) }
        }
    }

    // ImageButton을 CustomHomeChallenge로 교체하는 함수
    private fun replaceButtonWithCustomView(
        soloChallenge: UserSoloChallengeInfo?,
        crewChallenge: UserCrewChallengeInfo?
    ) {
        val parentLayout = binding.mainChallengeFrame
        val btnAddChallenge = parentLayout.findViewById<ImageButton>(R.id.btnAddChallenge)

        btnAddChallenge?.let {
            parentLayout.removeView(it)
        } // 기존 뷰 제거

        val customView = CustomHomeChallenge(requireContext())

        if (soloChallenge != null) {
            customView.updateSoloTitle(soloChallenge.challengeMateNickName)
            // Solo 챌린지 데이터 적용
            customView.updateDates(
                startDate = soloChallenge.soloStartDate,
                dayCount = soloChallenge.soloDayCount
            )
            customView.updatePeriodSolo(soloChallenge.challengePeriod)
            customView.updateDistance(soloChallenge.challengeDistance)
            customView.updateSoloImage(soloChallenge.challengeMateTendency)
        }

        if (crewChallenge != null) {
            // 크루 이름 색상 변경 및 "크루" 추가
            customView.updateCrewTitle(crewChallenge.crewName)
            // Crew 챌린지 데이터 적용
            customView.updateDates(
                startDate = crewChallenge.crewStartDate,
                dayCount = crewChallenge.crewDayCount
            )
            customView.updatePeriodCrew(crewChallenge.challengePeriod)

            // 크루원 성향에 따라 이미지 설정
            val crewTendencies = crewChallenge.myParticipantIdsInfo.map { it.memberTendency }
            customView.updateCrewImages(crewTendencies)
        }

        // Solo 챌린지 상태 아이콘 설정
        val soloChallengeLevel = when (soloChallenge?.status) {
            "PENDING" -> 0
            "IN_PROGRESS" -> 1
            "COMPLETED" -> 2
            else -> 0
        }

        // Crew 챌린지 상태 아이콘 설정
        val crewChallengeLevel = when (crewChallenge?.challengeStatus) {
            "PENDING" -> 0
            "IN_PROGRESS" -> 1
            "COMPLETED" -> 2
            else -> 0
        }

        // 선택된 챌린지에 따라 적절한 아이콘 적용
        if (soloChallenge != null) {
            customView.updateChallengeState(soloChallengeLevel) // Solo 챌린지 아이콘 적용
        } else if (crewChallenge != null) {
            customView.updateChallengeState(crewChallengeLevel) // Crew 챌린지 아이콘 적용
        }

        parentLayout.addView(customView) // 새로운 커스텀 뷰 추가
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchHomeChallengeData() // 다른 화면에서 돌아올 때 다시 요청
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}