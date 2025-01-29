package com.example.yourun.view.activities

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.yourun.R
import java.time.LocalDate
import java.time.YearMonth

class CalendarActivity : AppCompatActivity() {

    private lateinit var weekCalendarGrid: GridLayout
    private lateinit var fullCalendarGrid: GridLayout
    private lateinit var dragBar: View

    private var selectedDate: LocalDate = LocalDate.now()
    private var isFullCalendarVisible = false // 전체 달력 표시 여부


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

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


        // View 초기화
        weekCalendarGrid = findViewById(R.id.weekCalendarGrid)
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
        }

        // 다음 달 버튼 클릭 이벤트
        nextMonthButton.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1) // 다음 달로 이동
            updateMonthYearText(monthYearText) // 월 텍스트 업데이트
            setupWeekCalendar() // 주간 달력 업데이트
            setupFullCalendar() // 전체 달력 업데이트
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

        // 날짜 셀 크기 계산
        val cellSize = (resources.displayMetrics.widthPixels / 7)
        layoutParams.width = cellSize
        layoutParams.height = cellSize

        // 날짜 셀 외부 간격 설정
        layoutParams.setMargins(2, 2, 2, 2)

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
        }

        return textView
    }


    private fun createEmptyView(): TextView {
        val textView = TextView(this)
        val layoutParams = GridLayout.LayoutParams()

        layoutParams.width = resources.displayMetrics.widthPixels / 7
        layoutParams.height = layoutParams.width
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
