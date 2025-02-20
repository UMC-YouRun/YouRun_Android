package com.example.yourun.view.fragments

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
        mateAdapter = MateRankingAdapter(mateDataList, userInfo?.nickname ?: "") { mateId ->
            deleteMate(mateId)  // 🔹 삭제 요청 함수 호출
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mateAdapter
        }

        // 데이터 로드 (API 요청)
        loadUserAndMates()

        return view
    }

    private fun loadUserAndMates() {
        val token = ApiClient.TokenManager.getToken()
        Log.d("MateFragment", "불러온 토큰: $token")

        if (token.isNullOrEmpty()) {
            Log.e("MateFragment", "토큰이 없습니다. 로그인 상태를 확인하세요.")
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // ✅ 사용자 정보 가져오기
                val userInfo = mateRepository.getMyRunData()
                val mates = mateRepository.getMates()

                // 리스트 초기화
                mateDataList.clear()

                userInfo?.let { user ->
                    Log.d("MateFragment", "사용자 정보 로드 성공: ${user.nickname}")

                    // 🔹 사용자도 같은 방식으로 추가 (거리 변환 1/100)
                    val myMateData = MateData(
                        mateId = user.id,
                        rank = -1, // 나중에 정렬 후 업데이트
                        profileImageResId = getProfileImageByTendency(user.tendency),
                        nickname = user.nickname,
                        tags = user.tags ?: emptyList(),
                        countDay = (1 ..5).random(),
                        totalDistance = (600..700).random()/10,
                        change = 0,
                        tendency = user.tendency
                    )
                    mateDataList.add(myMateData)
                }

                // 🔹 메이트 리스트도 같은 변환 적용
                mates.forEach { mate ->
                    mate.totalDistance /= 100 // 🟢 1/100 변환 적용
                }

                // 🔹 메이트 데이터 추가 후 정렬 (총 거리 기준)
                mateDataList.addAll(mates)
                mateDataList.sortByDescending { it.totalDistance } // 🟢 거리 높은 순 정렬

                // 🔹 순위 업데이트
                mateDataList.forEachIndexed { index, mate ->
                    mate.rank = index + 1 // 🟢 사용자 포함한 전체 순위 재계산
                }

                // 🔹 변동값 생성 (수정된 로직)
                generateValidChanges(mateDataList)

                // 🔹 RecyclerView 업데이트
                userInfo?.let { user ->
                    mateAdapter = MateRankingAdapter(mateDataList, user.nickname) { mateId ->
                        deleteMate(mateId)
                    }
                    recyclerView.adapter = mateAdapter
                }

                // 🔹 Top 3 업데이트
                updateTop3Mates()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MateFragment", "API 호출 오류: ${e.message}")
            }
        }
    }

    private fun updateTop3Mates() {
        val topMates = mateDataList.sortedByDescending { it.totalDistance }.take(3)

        if (topMates.isNotEmpty()) {
            // 1등
            view?.findViewById<TextView>(R.id.name_mate_rank1)?.text = topMates[0].nickname
            view?.findViewById<TextView>(R.id.change_mate_rank1)?.text =
                if (topMates[0].change > 0) "+${topMates[0].change}위" else "${topMates[0].change}위"
            view?.findViewById<ImageView>(R.id.character_mate_rank1)?.setImageResource(getTop3ProfileImage(topMates[0].tendency))
            view?.findViewById<ImageView>(R.id.card_mate_rank1)?.setImageResource(getTop3BgImage(topMates[0].tendency))

            // 2등 (데이터 존재할 때만)
            if (topMates.size > 1) {
                view?.findViewById<TextView>(R.id.name_mate_rank2)?.text = topMates[1].nickname
                view?.findViewById<TextView>(R.id.change_mate_rank2)?.text =
                    if (topMates[1].change > 0) "+${topMates[1].change}위" else "${topMates[1].change}위"
                view?.findViewById<ImageView>(R.id.character_mate_rank2)?.setImageResource(getTop3ProfileImage(topMates[1].tendency))
                view?.findViewById<ImageView>(R.id.card_mate_rank2)?.setImageResource(getTop3BgImage(topMates[1].tendency))
            }

            // 3등 (데이터 존재할 때만)
            if (topMates.size > 2) {
                view?.findViewById<TextView>(R.id.name_mate_rank3)?.text = topMates[2].nickname
                view?.findViewById<TextView>(R.id.change_mate_rank3)?.text =
                    if (topMates[2].change > 0) "+${topMates[2].change}위" else "${topMates[2].change}위"
                view?.findViewById<ImageView>(R.id.characeter_mate_rank3)?.setImageResource(getTop3ProfileImage(topMates[2].tendency))
                view?.findViewById<ImageView>(R.id.card_mate_rank3)?.setImageResource(getTop3BgImage(topMates[2].tendency))
            }
        }
    }

    private fun getTop3ProfileImage(tendency: String?): Int {
        return when (tendency) {
            "페이스메이커" -> R.drawable.character_mate_pacemaker
            "트레일러너" -> R.drawable.img_mate_trailrunner
            "스프린터" -> R.drawable.img_mate_sprinter
            else -> R.drawable.character_mate_pacemaker // 기본 이미지
        }
    }

    private fun getTop3BgImage(tendency: String?): Int {
        return when (tendency) {
            "페이스메이커" -> R.drawable.bg_card_purple
            "트레일러너" -> R.drawable.bg_card_red
            "스프린터" -> R.drawable.bg_card_yellow
            else -> R.drawable.bg_card_purple // 기본 이미지
        }
    }

    private fun generateValidChanges(mateDataList: MutableList<MateData>) {
        val previousRanks = mutableSetOf<Int>()
        val validMates = mateDataList.filter { it.totalDistance > 0 }
        val listSize = validMates.size

        for (mate in mateDataList) {
            if (mate.totalDistance == 0) {
                mate.change = 0
                continue
            }

            var change: Int
            do {
                change = (-listSize / 2..listSize / 2).random()

                // 1등이 음수 변동값을 가지는 걸 막되, 0은 허용
                if (mate.rank == 1 && change > 0) continue

                if (mate.rank == listSize && change < 0) continue

                // 변동값을 적용한 등수가 1 이상, 리스트 크기 이하인지 확인
                if (!(1 <= mate.rank + change && mate.rank + change <= listSize)) continue

            } while ((mate.rank - change) in previousRanks)

            mate.change = change
            previousRanks.add(mate.rank - change)
        }
    }

    private fun getProfileImageByTendency(tendency: String?): Int {
        return when (tendency) {
            "페이스메이커" -> R.drawable.img_profile_pacemaker
            "트레일러너" -> R.drawable.img_profile_trailrunner_red
            "스프린터" -> R.drawable.img_profile_sprinter_yellow
            else -> R.drawable.img_profile_pacemaker // 경향 없을시 기본 이미지 불러오기
        }
    }

    private fun deleteMate(mateId: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = mateRepository.deleteMate(mateId)
                if (response.status == 200 && response.data == true) {
                    Log.d("MateFragment", "메이트 삭제 성공: $mateId")
                    mateDataList.removeAll { it.mateId == mateId } // 🔹 리스트에서 삭제
                    mateAdapter.notifyDataSetChanged()  // 🔹 RecyclerView 갱신
                } else {
                    Log.e("MateFragment", "메이트 삭제 실패")
                }
            } catch (e: Exception) {
                Log.e("MateFragment", "메이트 삭제 API 호출 오류: ${e.message}")
            }
        }
    }

}