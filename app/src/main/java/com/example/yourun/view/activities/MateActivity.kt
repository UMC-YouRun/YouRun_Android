package com.example.yourun.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.view.adapters.MateAdapter
import com.example.yourun.model.data.Mate

class MateActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mateAdapter: MateAdapter  // 어댑터 선언
    private val mateList = mutableListOf<Mate>()  // 데이터 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mate)

        // RecyclerView 설정
        recyclerView = findViewById(R.id.recycler_view)
        mateAdapter = MateAdapter(mateList)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MateActivity)
            adapter = mateAdapter
        }

        // 데이터 로드 (샘플 데이터 추가)
        loadMates()
    }

    private fun loadMates() {
        // 여기서 API 요청해서 데이터 불러오기

        mateList.add(Mate(1, R.drawable.img_profile_sprinter_purple, "청정원", 12, 20, +1))
        mateList.add(Mate(2, R.drawable.img_profile_trailrunner_red, "루시", 27, 18, -1))
        mateList.add(Mate(3, R.drawable.img_profile_sprinter_yellow, "루나", 28, 15, +2))

        // 어댑터에 변경 사항 반영
        mateAdapter.notifyDataSetChanged()
    }
}
