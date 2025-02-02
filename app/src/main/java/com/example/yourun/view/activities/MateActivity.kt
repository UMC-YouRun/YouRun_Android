package com.example.yourun.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.view.adapters.MateAdapter
import com.example.yourun.model.data.MateData
import com.example.yourun.model.repository.MateRepository
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.network.ApiClient.getAccessTokenFromSharedPreferences
import kotlinx.coroutines.launch

class MateActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mateAdapter: MateAdapter
    private val mateDataList = mutableListOf<MateData>()

    // MateRepository 인스턴스 생성
    private val mateRepository by lazy { MateRepository(ApiClient.getApiService(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mate)

        // RecyclerView 설정
        recyclerView = findViewById(R.id.recycler_view)
        mateAdapter = MateAdapter(mateDataList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MateActivity)
            adapter = mateAdapter
        }

        // 데이터 로드 (API 요청)
        loadMates()
    }

    private fun loadMates() {
        val token = getAccessTokenFromSharedPreferences(this)

        // 코루틴을 사용한 비동기 API 호출
        lifecycleScope.launch {
            try {
                val mates = mateRepository.getMates(token!!)
                mateDataList.addAll(mates)
                mateAdapter.notifyDataSetChanged()  // 데이터 변경 사항 어댑터에 반영
            } catch (e: Exception) {
                e.printStackTrace()
                // 예외 발생 시 로그 출력 또는 사용자에게 에러 메시지 표시
            }
        }
    }
}
