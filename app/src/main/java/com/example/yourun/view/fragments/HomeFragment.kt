package com.example.yourun.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.yourun.databinding.FragmentHomeBinding
import com.example.yourun.model.data.response.UserInfo
import com.example.yourun.model.data.response.ChallengeData
import com.example.yourun.model.data.response.UserCrewChallengeInfo
import com.example.yourun.model.data.response.UserMateInfo
import com.example.yourun.model.data.response.UserSoloChallengeInfo
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.HomeRepository
import com.example.yourun.view.activities.CalendarActivity
import com.example.yourun.view.activities.ChallengeListActivity
import com.example.yourun.view.activities.CreateChallengeActivity
import com.example.yourun.view.custom.CustomHomeChallenge
import com.example.yourun.view.custom.CustomMateView
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
            HomeRepository(ApiClient.getApiService()),
            requireActivity().application
        )
    }

    private var isCrewSelected = true // 초기값 : 크루 챌린지가 선택된 상태
    private var originalChallengeButton: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 서버에서 챌린지 데이터 가져오기, 처음 한 번 호출
        viewModel.fetchHomeChallengeData()
        viewModel.fetchRecommendMates() // 추천 메이트 데이터 가져오기
        viewModel.fetchUserInfo()

        // 유저 정보 관리
        viewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            userInfo?.let { safeUserInfo ->
                updateUserInfoText(safeUserInfo)
            }
        }

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

        // challengeButtonContainer에 기본 btnAddChallenge 뷰를 저장
        originalChallengeButton = binding.challengeButtonContainer.findViewById(R.id.btnAddChallenge)

        binding.btnAddChallenge.setOnClickListener {
            val intent = Intent(requireContext(), CreateChallengeActivity::class.java)
            startActivity(intent)
            parentFragmentManager.popBackStack()
        }

        // ViewModel의 챌린지 데이터 옵저빙
        viewModel.challengeData.observe(viewLifecycleOwner) { challengeData ->
            if (challengeData == null) {
                Log.d("HomeFragment","홈 챌린지 데이터를 불러오지 못했습니다.")
            } else {
                updateChallengeView(challengeData)
            }
        }

        binding.btnAddChallenge.setOnClickListener {
            val intent = Intent(requireContext(), ChallengeListActivity::class.java)
            startActivity(intent)
            parentFragmentManager.popBackStack()
        }

        binding.btnCalendar.setOnClickListener {
            val intent = Intent(requireContext(), CalendarActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddChallenge.setOnClickListener {
            val intent = Intent(requireContext(), CreateChallengeActivity::class.java)
            startActivity(intent)
            parentFragmentManager.popBackStack()
        }

        // 추천 메이트 UI 업데이트
        viewModel.recommendMates.observe(viewLifecycleOwner) { mates ->
            updateRecommendMatesUI(mates, viewModel, showHeart = true)
        }

        // btn_redirect 클릭 시 최신 메이트 데이터 다시 불러오기
        binding.btnRedirect.setOnClickListener {
            Log.d("HomeFragment", "btn_redirect 클릭됨 - 추천 메이트 갱신")
            viewModel.fetchRecommendMates()
        }
    }

    // 버튼 상태에 따라 챌린지 UI 업데이트
    private fun updateChallengeView(challengeData: ChallengeData? = viewModel.challengeData.value) {
        val container = binding.challengeButtonContainer
        container.removeAllViews()

        if (isCrewSelected) {
            // Crew 버튼 선택 시
            if (challengeData?.crewChallenge != null) {
                container.addView(createCustomHomeChallengeViewForCrew(challengeData.crewChallenge))
            } else {
                // crew 데이터가 없으면 기본 버튼 복원
                originalChallengeButton?.let { container.addView(it) }
            }
        } else {
            // Solo 버튼 선택 시
            if (challengeData?.soloChallenge != null) {
                container.addView(createCustomHomeChallengeViewForSolo(challengeData.soloChallenge))
            } else {
                // solo 데이터가 없으면 기본 버튼 복원
                originalChallengeButton?.let { container.addView(it) }
            }
        }
    }

    private fun createCustomHomeChallengeViewForSolo(soloChallenge: UserSoloChallengeInfo): CustomHomeChallenge {
        val customView = CustomHomeChallenge(requireContext()).apply {
            id = R.id.custom_home_challenge_view
        }
        customView.updateSoloTitle(soloChallenge.challengeMateNickName)
        customView.updateDates(startDate = soloChallenge.soloStartDate, dayCount = soloChallenge.soloDayCount)
        customView.updatePeriodSolo(soloChallenge.challengePeriod)
        customView.updateDistance(soloChallenge.challengeDistance)
        customView.updateSoloImage(soloChallenge.challengeMateTendency)
        val soloChallengeLevel = when (soloChallenge.status) {
            "PENDING" -> 0
            "IN_PROGRESS" -> 1
            "COMPLETED" -> 2
            else -> 0
        }
        customView.updateChallengeState(soloChallengeLevel)
        return customView
    }

    private fun createCustomHomeChallengeViewForCrew(crewChallenge: UserCrewChallengeInfo): CustomHomeChallenge {
        val customView = CustomHomeChallenge(requireContext()).apply {
            id = R.id.custom_home_challenge_view
        }
        customView.updateCrewTitle(crewChallenge.crewName)
        customView.updateDates(startDate = crewChallenge.crewStartDate, dayCount = crewChallenge.crewDayCount)
        customView.updatePeriodCrew(crewChallenge.challengePeriod)
        val crewTendencies = crewChallenge.myParticipantIdsInfo.map { it.memberTendency }
        customView.updateCrewImages(crewTendencies)
        val crewChallengeLevel = when (crewChallenge.challengeStatus) {
            "PENDING" -> 0
            "IN_PROGRESS" -> 1
            "COMPLETED" -> 2
            else -> 0
        }
        customView.updateChallengeState(crewChallengeLevel)
        return customView
    }

    // 추천 메이트 UI 추가
    private fun <T: ViewModel> updateRecommendMatesUI(mates: List<UserMateInfo>, viewModel: T, showHeart: Boolean) {
        val parentLayout = binding.mainLinearLayout
        val referenceView = binding.viewHomeMate // 기존 view_home_mate 아래에 추가
        val referenceIndex = parentLayout.indexOfChild(referenceView)

        // 기존의 CustomMateView 삭제 (이전 추천 메이트 삭제)
        parentLayout.children.filter { it is CustomMateView<*> || it.tag == "divider" }.forEach {
            parentLayout.removeView(it) }

        // 추천 메이트 목록 추가 (최대 5개)
        mates.take(5).forEachIndexed { index, mate ->
            // 첫 번째 뷰가 아닐 때만 가로 구분선 추가
            if (index > 0) {
                val dividerView = View(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.dpToPx(requireContext()) // 경계선 높이 1dp
                    ).apply {
                        setMargins(24.dpToPx(requireContext()), 0, 24.dpToPx(requireContext()), 4.dpToPx(requireContext()))
                    }
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.border)) // 경계선 색상
                    tag = "divider" // 제거할 때 구분하기 위해 태그 추가
                }
                parentLayout.addView(dividerView, referenceIndex + 1) // 가로 경계선 추가
            }

            val mateView = CustomMateView<T>(requireContext(), showHeartButton = showHeart).apply {
                setViewModel(viewModel)
                updateMateInfo(mate, mates.size - index)

                // 상단 마진을 최소화하여 뷰를 더 위로 붙이기
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                    setMargins(24.dpToPx(requireContext()), 0, 24.dpToPx(requireContext()), 6.dpToPx(requireContext()))
                }
            }

            // view_home_mate 바로 아래에 추가
            parentLayout.addView(mateView, referenceIndex + 1)
        }
    }

    // dp → px 변환 함수
    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun updateUserInfoText(userInfo: UserInfo) {

        val sharedPref = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userNickname = userInfo.nickname
        val signupDateStr = sharedPref.getString("signup_date", "") ?: ""
        val crewReward = userInfo.crewReward
        val soloReward = userInfo.personalReward
        val userTendency = userInfo.tendency

        if (userNickname.isNotEmpty()) {
            val originalRunText = getString(R.string.main_run_together)
            val originalSimilarMateText = getString(R.string.similar_mate)

            binding.txtMainRunTogether.text = "$userNickname$originalRunText"
            binding.txtMainUserSimilarMate.text = "$userNickname$originalSimilarMateText"
        }

        if (signupDateStr.isNotEmpty()) {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val signupDate = dateFormat.parse(signupDateStr) ?: return
                val currentDate = Date()

                val diffInMillis = currentDate.time - signupDate.time
                val daysUsed = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

                binding.txtMainRunDay.text = "$daysUsed 일째!"
            } catch (e: ParseException) {
                Log.e("HomeFragment", "날짜 파싱 오류", e)
            }
        }

        binding.txtMainCrewReward.text = "${crewReward}개"
        binding.txtMainSoloReward.text = "${soloReward}개"

        val imageRes = when (userTendency) {
            "페이스메이커" -> R.drawable.img_home_facemaker
            "스프린터" -> R.drawable.img_home_sprinter
            "트레일러너" -> R.drawable.img_home_trailrunner
            else -> R.drawable.img_home_facemaker // 기본 이미지
        }

        binding.imgHomeCharacter.setImageResource(imageRes)
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