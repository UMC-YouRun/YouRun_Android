package com.example.yourun.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.model.data.MateData
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.MateRepository
import com.example.yourun.utils.TokenManager
import com.example.yourun.view.adapters.MateAdapter
import kotlinx.coroutines.launch

class MateFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mateAdapter: MateAdapter
    private val mateDataList = mutableListOf<MateData>()

    // MateRepository 인스턴스 생성
    private val mateRepository by lazy { MateRepository(ApiClient.getApiService()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mate, container, false)

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.recycler_view)
        mateAdapter = MateAdapter(mateDataList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mateAdapter
        }

        // 데이터 로드 (API 요청)
        loadMates()

        return view
    }

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
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MateFragment", "API 호출 오류: ${e.message}")
            }
        }
    }

}