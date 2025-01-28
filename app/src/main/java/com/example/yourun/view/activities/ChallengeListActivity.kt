package com.example.yourun.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yourun.view.adapters.ChallengeAdapter
import com.example.yourun.model.data.ChallengeItem

class ChallengeListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var refreshHint: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_list)

        recyclerView = findViewById(R.id.recycler_view)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        refreshHint = findViewById(R.id.refresh_hint)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChallengeAdapter(getSampleData())

        // 새로고침 동작 설정
        swipeRefreshLayout.setOnRefreshListener {
            // 데이터를 갱신하는 로직 추가
            refreshData()
        }

        // 스크롤 상태 감지
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(-1)) {
                    // 스크롤이 맨 위일 때 새로고침 힌트를 표시
                    refreshHint.visibility = View.VISIBLE
                } else {
                    // 스크롤이 진행 중이면 새로고침 힌트를 숨김
                    refreshHint.visibility = View.GONE
                }
            }
        })
    }

    private fun refreshData() {
        // 예제: 데이터를 다시 불러온 뒤 RecyclerView 갱신
        (recyclerView.adapter as ChallengeAdapter).updateData(getSampleData())
        swipeRefreshLayout.isRefreshing = false // 새로고침 완료
    }

    private fun getSampleData(): List<ChallengeItem> {
        return listOf(
            ChallengeItem(
                badgeImage = R.drawable.img_crew_badge_count,
                title = "3일 연속 3km 러닝!",
                description = "챌린지 메이트 루시와 함께!",
                members = listOf(
                    R.drawable.img_mini2_pacemaker,
                    R.drawable.img_mini2_trailrunner,
                    R.drawable.img_mini2_sprinter
                ),
                remaining = "남은 1명!"
            )
            // 추가 데이터...
        )
    }
}