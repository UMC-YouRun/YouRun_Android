package com.example.yourun.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.yourun.R
import com.example.yourun.databinding.FragmentHomeBinding
import com.example.yourun.model.data.ChallengeData
import com.example.yourun.model.data.UserCrewChallengeInfo
import com.example.yourun.model.data.UserSoloChallengeInfo
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.HomeChallengeRepository
import com.example.yourun.view.custom.CustomHomeChallenge
import com.example.yourun.viewmodel.HomeViewModel
import com.example.yourun.viewmodel.HomeViewModelFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            HomeChallengeRepository(ApiClient.getHomeApiService()),
            requireActivity().application
        )
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

        updateRunTogetherText()
        updateUsageDays()

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
        viewModel.challengeData.observe(viewLifecycleOwner) { challengeData ->
            updateChallengeView(challengeData) // 데이터 변경될 때 UI 업데이트
        }

        binding.btnAddChallenge.setOnClickListener {
            // 챌린지 추가 화면 이동
        }
    }

    // 버튼 상태에 따라 챌린지 UI 업데이트
    private fun updateChallengeView(challengeData: ChallengeData? = viewModel.challengeData.value) {
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

        // soloChallenge와 crewChallenge가 모두 null이면 기존 뷰 유지하고 종료
        if (soloChallenge == null && crewChallenge == null) {
            Log.d("HomeFragment", "챌린지 데이터 없음: 기존 뷰 유지")
            return
        }

        btnAddChallenge?.let {
            parentLayout.removeView(it)
        } // 기존 뷰 제거

        val customView = CustomHomeChallenge(requireContext())

        soloChallenge?.let {
            customView.updateSoloTitle(it.challengeMateNickName)
            customView.updateDates(startDate = it.soloStartDate, dayCount = it.soloDayCount)
            customView.updatePeriodSolo(it.challengePeriod)
            customView.updateDistance(it.challengeDistance)
            customView.updateSoloImage(it.challengeMateTendency)

            val soloChallengeLevel = when (it.status) {
                "PENDING" -> 0
                "IN_PROGRESS" -> 1
                "COMPLETED" -> 2
                else -> 0
            }
            customView.updateChallengeState(soloChallengeLevel) // Solo 챌린지 상태 아이콘 적용
        }

        crewChallenge?.let {
            customView.updateCrewTitle(it.crewName)
            customView.updateDates(startDate = it.crewStartDate, dayCount = it.crewDayCount)
            customView.updatePeriodCrew(it.challengePeriod)

            val crewTendencies = it.myParticipantIdsInfo.map { member -> member.memberTendency }
            customView.updateCrewImages(crewTendencies)

            val crewChallengeLevel = when (it.challengeStatus) {
                "PENDING" -> 0
                "IN_PROGRESS" -> 1
                "COMPLETED" -> 2
                else -> 0
            }
            customView.updateChallengeState(crewChallengeLevel) // Crew 챌린지 상태 아이콘 적용
        }

        parentLayout.addView(customView) // 새로운 커스텀 뷰 추가
    }

    private fun updateRunTogetherText() {
        val sharedPref = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userNickname = sharedPref.getString("nickname", "") ?: ""

        val runTogetherTextView = binding.txtMainRunTogether
        val originalText = getString(R.string.main_run_together) // 기존 텍스트

        if (userNickname.isNotEmpty()) {
            val finalText = "$userNickname$originalText"
            runTogetherTextView.text = finalText
        }
    }

    private fun updateUsageDays() {
        val sharedPref = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val signupDateStr = sharedPref.getString("signup_date", "") ?: ""

        if (signupDateStr.isNotEmpty()) {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val signupDate = dateFormat.parse(signupDateStr) ?: return
                val currentDate = Date()

                // 날짜 차이 계산
                val diffInMillis = currentDate.time - signupDate.time
                val daysUsed = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

                // UI 업데이트
                val usageTextView = binding.txtMainRunDay
                usageTextView.text = "$daysUsed 일째!"
            } catch (e: ParseException) {
                Log.e("HomeFragment", "날짜 파싱 오류", e)
            }
        }
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