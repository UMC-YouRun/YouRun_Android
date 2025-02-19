/*package com.example.yourun.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.model.data.MateData
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.MateRepository
import com.example.yourun.model.data.response.UserInfo
import com.example.yourun.utils.TokenManager
import com.example.yourun.view.adapters.MateRankingAdapter
import kotlinx.coroutines.launch


class MateFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mateAdapter: MateRankingAdapter
    private val mateDataList = mutableListOf<MateData>()
    private var userInfo: UserInfo? = null  // 사용자 정보 저장

    // MateRepository 인스턴스 생성
    private val mateRepository by lazy { MateRepository(ApiClient.getApiService()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mate, container, false)

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.recycler_view)
        mateAdapter = MateRankingAdapter(mateDataList, userInfo?.nickname ?: "")
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mateAdapter
        }

        // 데이터 로드 (API 요청)
        loadUserAndMates()

        return view
    }

    private fun loadUserAndMates() {
        val token = TokenManager.getInstance(requireContext()).getToken()
        Log.d("MateFragment", "불러온 토큰: $token")

        if (token.isNullOrEmpty()) {
            Log.e("MateFragment", "토큰이 없습니다. 로그인 상태를 확인하세요.")
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // ✅ 내 정보 가져오기 (UserInfo 타입으로 반환)
                val userInfo = mateRepository.getMyRunData()

                // ✅ 친구(메이트) 정보 조회
                // val mates = mateRepository.getMates()

                // 리스트 초기화
                mateDataList.clear()


                userInfo?.let { user ->
                    Log.d("MateFragment", "사용자 정보 로드 성공: ${user.nickname}")

                    // 🔹 내 정보를 MateData 형태로 변환하여 추가
                    val myMateData = MateData(
                        rank = -1,  // 순위는 나중에 정렬 후 업데이트
                        profileImageResId = getProfileImageByTendency(user.tendency),
                        nickname = user.nickname,
                        tags = user.tags ?: emptyList(),
                        countDay = 0,
                        totalDistance = 0,
                        change = 0,
                        tendency = user.tendency
                    )
                    mateDataList.add(myMateData) // 먼저 추가
                }

                // 🔹 친구(메이트) 정보 추가
                // mateDataList.addAll(mates)

                // 🔹 전체 정렬 (예: 총 거리 기준)
                mateDataList.sortByDescending { it.totalDistance }

                // 🔹 순위 업데이트
                mateDataList.forEachIndexed { index, mate ->
                    mate.rank = index + 1 // rank를 var로 변경해서 업데이트 가능하게 함
                }

                // 🔹 RecyclerView Adapter 업데이트
                userInfo?.let { user ->
                    mateAdapter = MateRankingAdapter(mateDataList, user.nickname)
                    recyclerView.adapter = mateAdapter
                }

                // 🔹 상위 3명 러너 업데이트
                updateTop3Mates()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MateFragment", "API 호출 오류: ${e.message}")
            }
        }
    }



    /*
    private fun loadMates() {
        val token = TokenManager.getInstance(requireContext()).getToken()
        Log.d("MateFragment", "불러온 토큰: $token")

        if (token.isNullOrEmpty()) {
            Log.e("MateFragment", "토큰이 없습니다. 로그인 상태를 확인하세요.")
            return  // 토큰이 없으면 API 호출을 하지 않음
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val mates = mateRepository.getMates("Bearer $token") // Authorization 헤더 적용
                if (mates.isEmpty()) {
                    Log.d("MateFragment", "메이트 데이터가 없습니다.")
                } else {
                    Log.d("MateFragment", "메이트 데이터 로드 성공: ${mates.size}명")
                }
                mateDataList.clear()
                mateDataList.addAll(mates)
                mateAdapter.notifyDataSetChanged()

                // 🔹 상위 3명 러너 정렬하여 UI 반영
                updateTop3Mates()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MateFragment", "API 호출 오류: ${e.message}")
            }
        }
    }
     */

    private fun updateTop3Mates() {
        val topMates = mateDataList.sortedByDescending { it.totalDistance }.take(3)

        if (topMates.isNotEmpty()) {
            // 1등
            view?.findViewById<TextView>(R.id.name_mate_rank1)?.text = topMates[0].nickname
            view?.findViewById<TextView>(R.id.change_mate_rank1)?.text = "${topMates[0].change}위"
            view?.findViewById<ImageView>(R.id.character_mate_rank1)?.setImageResource(getTop3ProfileImage(topMates[0].tendency))

            // 2등 (데이터 존재할 때만)
            if (topMates.size > 1) {
                view?.findViewById<TextView>(R.id.name_mate_rank2)?.text = topMates[1].nickname
                view?.findViewById<TextView>(R.id.change_mate_rank2)?.text = "${topMates[1].change}위"
                view?.findViewById<ImageView>(R.id.character_mate_rank2)?.setImageResource(getTop3ProfileImage(topMates[1].tendency))
            }

            // 3등 (데이터 존재할 때만)
            if (topMates.size > 2) {
                view?.findViewById<TextView>(R.id.name_mate_rank3)?.text = topMates[2].nickname
                view?.findViewById<TextView>(R.id.change_mate_rank3)?.text = "${topMates[2].change}위"
                view?.findViewById<ImageView>(R.id.characeter_mate_rank3)?.setImageResource(getTop3ProfileImage(topMates[2].tendency))
            }
        }
    }

    private fun getTop3ProfileImage(tendency: String?): Int {
        return when (tendency) {
            "페이스메이커" -> R.drawable.img_mate_pacemaker
            "트레일러너" -> R.drawable.img_mate_trailrunner
            "스프린터" -> R.drawable.img_mate_sprinter
            else -> R.drawable.img_mate_pacemaker // 기본 이미지
        }
    }

    private fun getProfileImageByTendency(tendency: String?): Int {
        return when (tendency) {
            "페이스메이커" -> R.drawable.img_profile_pacemaker_purple
            "트레일러너" -> R.drawable.img_profile_trailrunner_red
            "스프린터" -> R.drawable.img_profile_sprinter_yellow
            else -> R.drawable.img_profile_pacemaker_purple  // 경향 없을시 기본 이미지 불러오기
        }
    }

}*/