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
import android.util.Log

class ChallengeListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var challengeAdapter: ChallengeAdapter
    private var isLoading = false // 로딩 상태 확인 변수
    private val refreshThreshold = 200 // 추가 스크롤 감지 기준(px)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_list)

        recyclerView = findViewById(R.id.recycler_view)
        challengeAdapter = ChallengeAdapter(getSampleData().toMutableList())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = challengeAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var isLoading = false
            private var isInitialized = false  // 🚀 초기 상태 여부 추가

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                Log.d("SCROLL_EVENT", "dx: $dx, dy: $dy")

                // 🚀 처음 실행될 때는 바로 체크하지 않음
                if (!isInitialized) {
                    isInitialized = true
                    return
                }

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                // ✅ 사용자가 스크롤해서 리스트의 끝에 도달한 경우에만 새로고침 실행
                if (!recyclerView.canScrollVertically(1) && dy >= 0) { // 🚀 dy >= 0 추가
                    Log.d("SCROLL_EVENT", "RecyclerView 끝에 도달!")

                    if (!isLoading) {
                        isLoading = true
                        showFooter(true)
                        refreshData()
                    }
                }
            }

            private fun refreshData() {
                Log.d("SCROLL_EVENT", "새로고침 시작!")
                recyclerView.postDelayed({
                    showFooter(false)
                    isLoading = false
                    Log.d("SCROLL_EVENT", "새로고침 완료 후 푸터 숨김")
                }, 2000) // 2초 후 새로고침 완료
            }

            private fun showFooter(visible: Boolean) {
                (recyclerView.adapter as ChallengeAdapter).let {
                    if (visible) {
                        it.notifyItemInserted(it.itemCount - 1)
                    } else {
                        it.notifyItemRemoved(it.itemCount - 1)
                    }
                }
            }
        })
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
        )
    }
}
