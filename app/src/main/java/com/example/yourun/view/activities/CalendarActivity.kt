package com.example.yourun.view.activities

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.model.data.response.RunningStatsResponse
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class CalendarActivity : AppCompatActivity() {

    private lateinit var weekCalendarGrid: GridLayout
    private lateinit var fullCalendarGrid: GridLayout
    private lateinit var dragBar: View

    private lateinit var distanceTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var paceTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var statsDate: TextView

    private var selectedDate: LocalDate = LocalDate.now()
    private var isFullCalendarVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val topBarTitle: TextView = findViewById(R.id.txtTopBarWithBackButton)

        topBarTitle.text = "나의 러닝 기록"

        val backButton: ImageButton = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        statsDate = findViewById(R.id.statsDate)

        val days = listOf("일", "월", "화", "수", "목", "금", "토")
        val textColor = ContextCompat.getColor(this, R.color.gray_600)
        val weekdayLayout = findViewById<LinearLayout>(R.id.weekdayLayout)

        days.forEach { day ->
            val textView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                gravity = Gravity.CENTER
                text = day
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                setTextColor(textColor)
            }
            weekdayLayout.addView(textView)
        }

        distanceTextView = findViewById(R.id.distance)
        timeTextView = findViewById(R.id.time)
        paceTextView = findViewById(R.id.pace)
        dateTextView = findViewById(R.id.statsDate)

        fetchRunningStats(selectedDate.year, selectedDate.monthValue)

        // View 초기화
        weekCalendarGrid = findViewById(R.id.dateLayout)
        fullCalendarGrid = findViewById(R.id.fullCalendarGrid)
        dragBar = findViewById(R.id.dragBar)

        val monthYearText: TextView = findViewById(R.id.monthYearText)
        val prevMonthButton: ImageView = findViewById(R.id.prevMonthButton)
        val nextMonthButton: ImageView = findViewById(R.id.nextMonthButton)

        // 초기 월 표시
        updateMonthYearText(monthYearText)

        // 이전 달 버튼 클릭 이벤트
        prevMonthButton.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1) // 이전 달로 이동
            updateMonthYearText(monthYearText) // 월 텍스트 업데이트
            setupWeekCalendar() // 주간 달력 업데이트
            setupFullCalendar() // 전체 달력 업데이트
            fetchRunningStats(selectedDate.year, selectedDate.monthValue)
        }

        // 다음 달 버튼 클릭 이벤트
        nextMonthButton.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1) // 다음 달로 이동
            updateMonthYearText(monthYearText) // 월 텍스트 업데이트
            setupWeekCalendar() // 주간 달력 업데이트
            setupFullCalendar() // 전체 달력 업데이트
            fetchRunningStats(selectedDate.year, selectedDate.monthValue)
        }

        // 드래그바 클릭/드래그 동작 설정
        dragBar.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                toggleCalendarView()
            }
            true
        }

        // 캘린더 초기화
        setupWeekCalendar()
        setupFullCalendar()

        // 초기 드래그바 위치 설정
        updateDragBarPosition()
    }

    private fun fetchRunningStats(year: Int, month: Int) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.getApiService().getRunningStats(year, month)

                if (response.isSuccessful) {
                    val runningStats = response.body()?.data ?: emptyList()
                    updateUI(runningStats)
                } else {
                    Log.e("RunningStats", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("RunningStats", "Exception: ${e.message}")
            }
        }
    }


    private fun updateUI(stats: List<RunningStatsResponse>) {
        if (stats.isNotEmpty()) {
            // ✅ 선택된 날짜에 해당하는 데이터 찾기 (날짜 부분만 비교)
            val selectedStats = stats.find { it.createdAt.substring(0, 10) == selectedDate.toString() }

            runOnUiThread {
                if (selectedStats != null) {
                    val distanceInKm = selectedStats.totalDistance / 1000
                    val totalTimeInMinutes = selectedStats.totalTime / 60000

                    statsDate.text = formatDate(selectedDate) // ✅ 선택한 날짜 표시

                    // ✅ 데이터 업데이트
                    distanceTextView.text = "${distanceInKm}km"
                    timeTextView.text = "${totalTimeInMinutes}분"
                    paceTextView.text = "${calculatePace(selectedStats.totalDistance, selectedStats.totalTime)}"
                } else {
                    // ✅ 선택한 날짜에 데이터가 없을 경우
                    statsDate.text = formatDate(selectedDate)
                    distanceTextView.text = "0km"
                    timeTextView.text = "0분"
                    paceTextView.text = "0'00km"
                }
            }
        }
    }

    private fun formatDate(date: LocalDate): String {
        return String.format("%04d년 %02d월 %02d일", date.year, date.monthValue, date.dayOfMonth)
    }


    private fun calculatePace(distance: Int, time: Int): String {
        if (distance == 0 || time == 0) return "00'0km"

        val pace = time.toDouble() / distance // ✅ (시간(ms) / 거리(m)) → (초 / m)
        val minutes = (pace / 60).toInt() // ✅ 분 계산
        val seconds = ((pace % 60) / 10).toInt() // ✅ 초를 10 단위로 반올림

        return String.format("%02d'%01dkm", minutes, seconds)
    }




    private fun updateMonthYearText(monthYearText: TextView) {
        val year = selectedDate.year
        val month = selectedDate.monthValue
        monthYearText.text = String.format("%d년 %d월", year, month)
    }


    private fun toggleCalendarView() {
        if (isFullCalendarVisible) {
            // 전체 달력 숨기기
            fullCalendarGrid.visibility = View.GONE
            weekCalendarGrid.visibility = View.VISIBLE
            isFullCalendarVisible = false
        } else {
            // 전체 달력 보이기
            fullCalendarGrid.visibility = View.VISIBLE
            weekCalendarGrid.visibility = View.GONE
            isFullCalendarVisible = true
        }

        // 드래그바 위치 업데이트
        updateDragBarPosition()
    }

    private fun updateDragBarPosition() {
        val params = dragBar.layoutParams as LinearLayout.LayoutParams
        if (isFullCalendarVisible) {
            // 전체 달력 아래에 위치
            params.topMargin = resources.getDimensionPixelSize(R.dimen.dragbar_margin_expanded)
        } else {
            // 주간 달력 아래에 위치
            params.topMargin = resources.getDimensionPixelSize(R.dimen.dragbar_margin_collapsed)
        }
        dragBar.layoutParams = params
    }


    private fun setupWeekCalendar() {
        val weekDates = getCurrentWeekDates(selectedDate)
        weekCalendarGrid.removeAllViews()
        weekDates.forEach { date ->
            val dateView = createDateView(date.dayOfMonth, date)
            weekCalendarGrid.addView(dateView)
        }
    }

    private fun setupFullCalendar() {
        val yearMonth = YearMonth.from(selectedDate)
        val firstDayOfMonth = yearMonth.atDay(1).dayOfWeek.value % 7
        val daysInMonth = yearMonth.lengthOfMonth()

        fullCalendarGrid.removeAllViews()
        for (i in 0 until firstDayOfMonth) {
            fullCalendarGrid.addView(createEmptyView())
        }
        for (day in 1..daysInMonth) {
            val currentDate = yearMonth.atDay(day)
            fullCalendarGrid.addView(createDateView(day, currentDate))
        }
        while (fullCalendarGrid.childCount % 7 != 0) {
            fullCalendarGrid.addView(createEmptyView())
        }
    }

    private fun createDateView(day: Int, currentDate: LocalDate): TextView {
        val textView = TextView(this)
        val layoutParams = GridLayout.LayoutParams()

        val totalWidth = resources.displayMetrics.widthPixels
        val margin = 0
        val cellSize = (totalWidth - margin * 14) / 8

        layoutParams.width = cellSize
        layoutParams.height = cellSize
        layoutParams.setMargins(margin, margin, margin, margin)

        textView.layoutParams = layoutParams
        textView.text = day.toString()
        textView.textSize = 16f
        textView.gravity = Gravity.CENTER

        // 선택된 날짜 강조
        if (currentDate == selectedDate) {
            textView.setBackgroundResource(R.drawable.ic_selected)
        } else {
            textView.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }

        textView.setOnClickListener {
            selectedDate = currentDate
            setupWeekCalendar()
            setupFullCalendar()
            fetchRunningStats(selectedDate.year, selectedDate.monthValue)
        }

        return textView
    }



    private fun createEmptyView(): TextView {
        val textView = TextView(this)
        val layoutParams = GridLayout.LayoutParams()

        // 빈 셀 크기 계산 (날짜 셀과 동일)
        val cellSize = resources.displayMetrics.widthPixels / 8
        layoutParams.width = cellSize
        layoutParams.height = cellSize

        layoutParams.setMargins(0, 0, 0, 0) // 마진 제거
        textView.layoutParams = layoutParams
        textView.text = ""
        textView.gravity = Gravity.CENTER

        return textView
    }

    private fun getCurrentWeekDates(date: LocalDate): List<LocalDate> {
        val startOfWeek = date.minusDays((date.dayOfWeek.value - 1).toLong())
        return (0 until 7).map { startOfWeek.plusDays(it.toLong()) }
    }
}
