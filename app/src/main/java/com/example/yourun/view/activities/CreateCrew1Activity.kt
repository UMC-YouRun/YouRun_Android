package com.example.yourun.view.activities
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.yourun.R
import com.example.yourun.databinding.ActivityCreateCrew1Binding
import java.time.LocalDate



class CreateCrew1Activity : AppCompatActivity () {
    private lateinit var binding: ActivityCreateCrew1Binding
    private lateinit var weekCalendarGrid: GridLayout
    private var selectedStartDate: LocalDate? = null
    private var selectedEndDate: LocalDate? = null
    private var selectedDate: LocalDate = LocalDate.now()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCrew1Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.nextBtn.setOnClickListener{
            val intent= Intent(this, CreateCrew2Activity::class.java)
            startActivity(intent)
        }


        weekCalendarGrid = findViewById(R.id.weekCalendarGrid)

        // 주간 달력 설정
        setupWeekCalendar()


        val days = listOf("일", "월", "화", "수", "목", "금", "토")
        val textColor = ContextCompat.getColor(this, R.color.gray)
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

        val monthYearText: TextView = findViewById(R.id.monthYearText)

        // 초기 월 표시
        updateMonthYearText(monthYearText)

        // 주간 달력 터치 이벤트 처리
        weekCalendarGrid.setOnTouchListener { v, event ->

            v.performClick()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 드래그 시작: 선택된 날짜의 시작
                    selectedStartDate = getDateFromEvent(event)
                    selectedEndDate = selectedStartDate
                    highlightSelectedDates() // 날짜 강조
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    // 드래그 이동: 선택 날짜 업데이트
                    selectedEndDate = getDateFromEvent(event)
                    highlightSelectedDates() // 날짜 강조
                    true
                }

                MotionEvent.ACTION_UP -> {
                    // 드래그 종료: 선택된 날짜 최종 적용
                    selectedEndDate = getDateFromEvent(event)
                    highlightSelectedDates() // 날짜 강조
                    true
                }

                else -> false
            }
        }
    }

    // 월/년 텍스트 업데이트
    private fun updateMonthYearText(monthYearText: TextView) {
        val year = selectedDate.year
        val month = selectedDate.monthValue
        monthYearText.text = String.format("%d년 %d월", year, month)
    }

    // 주간 달력 설정
    private fun setupWeekCalendar() {
        val weekDates = getCurrentWeekDates(selectedDate)
        weekCalendarGrid.removeAllViews()

        weekDates.forEach { date ->
            // 날짜와 함께 요일 정보(0~6, 0은 일요일) 전달
            val weekday = date.dayOfWeek.value - 1 // '일요일'을 0으로 설정
            val dateView = createDateView(date.dayOfMonth, date, weekday)
            weekCalendarGrid.addView(dateView)
        }
    }

    // 날짜 셀 생성
    private fun createDateView(day: Int, currentDate: LocalDate, weekday: Int): TextView {
        val textView = TextView(this)
        val layoutParams = GridLayout.LayoutParams()

        // 화면의 가로 너비에서 각 셀 크기 계산
        val totalWidth = resources.displayMetrics.widthPixels
        val margin = 4
        val cellSize = (totalWidth - margin * 14) / 7  // 7개의 칸에 맞게 크기 설정

        // 첫 번째 날짜 셀에 대해 요일에 맞춰 마진 설정
        if (weekday != 0) { // "일"요일이면 첫 번째 셀, 마진 없이 시작
            layoutParams.setMargins(margin * weekday, margin, margin, margin) // 요일에 맞게 마진 설정
        } else {
            layoutParams.setMargins(0, margin, margin, margin) // "일요일"은 마진을 제거
        }

        layoutParams.width = cellSize
        layoutParams.height = cellSize

        textView.layoutParams = layoutParams
        textView.text = day.toString()
        textView.textSize = 16f
        textView.gravity = Gravity.CENTER

        // 날짜 클릭 리스너
        textView.setOnClickListener {
            selectedStartDate = currentDate
            selectedEndDate = selectedStartDate
            highlightSelectedDates() // 날짜 강조
        }

        // 선택된 날짜 강조
        highlightSelectedDay(currentDate, textView)

        return textView
    }

    // 선택된 날짜 강조
    private fun highlightSelectedDay(currentDate: LocalDate, textView: TextView) {
        when {
            selectedStartDate != null && currentDate.isEqual(selectedStartDate) -> {
                // 시작일: 주황색 동그라미
                textView.setBackgroundResource(R.drawable.selected_date_orange)
                textView.setTextColor(ContextCompat.getColor(this, R.color.white)) // 텍스트 색상 변경
            }
            selectedEndDate != null && currentDate.isEqual(selectedEndDate) -> {
                // 종료일: 주황색 동그라미
                textView.setBackgroundResource(R.drawable.selected_date_orange)
                textView.setTextColor(ContextCompat.getColor(this, R.color.gray)) // 텍스트 색상 변경
            }
            selectedStartDate != null && selectedEndDate != null &&
                    currentDate.isAfter(selectedStartDate) && currentDate.isBefore(selectedEndDate) -> {
                // 범위에 포함된 날짜: 연속적인 색상 (그라디언트)
                textView.setBackgroundResource(R.drawable.range_gradient_yellow)
                textView.setTextColor(ContextCompat.getColor(this, R.color.black)) // 텍스트 색상 기본으로
            }
            currentDate.isEqual(LocalDate.now()) -> {
                // 오늘 날짜: 보라색 동그라미
                textView.setBackgroundResource(R.drawable.current_date_purple)
                textView.setTextColor(ContextCompat.getColor(this, R.color.black)) // 텍스트 색상 변경
            }
            else -> {
                // 선택되지 않은 날짜는 기본 상태로
                textView.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                textView.setTextColor(ContextCompat.getColor(this, R.color.black)) // 기본 텍스트 색상
            }
        }
    }

    // 드래그한 위치의 날짜를 반환
    private fun getDateFromEvent(event: MotionEvent): LocalDate {
        val cellWidth = weekCalendarGrid.width / 7
        val dayIndex = (event.x / cellWidth).toInt()
        return getCurrentWeekDates(selectedDate)[dayIndex]
    }

    // 주간 날짜를 계산
    private fun getCurrentWeekDates(date: LocalDate): List<LocalDate> {
        val todayDayOfWeek = date.dayOfWeek.value
        val startOfWeek = date.minusDays((todayDayOfWeek - 1).toLong())
        return (0 until 7).map { startOfWeek.plusDays(it.toLong()) }
    }

    // 선택된 날짜 강조
    private fun highlightSelectedDates() {
        weekCalendarGrid.children.forEach { view ->
            if (view is TextView) {
                val dayText = view.text.toString().toInt()
                val currentDate = selectedDate.plusDays(dayText.toLong() - 1)

                when {
                    selectedStartDate != null && currentDate.isEqual(selectedStartDate) -> {
                        view.setBackgroundResource(R.drawable.selected_date_orange)
                        view.setTextColor(ContextCompat.getColor(this, R.color.white))
                    }
                    selectedEndDate != null && currentDate.isEqual(selectedEndDate) -> {
                        view.setBackgroundResource(R.drawable.selected_date_orange)
                        view.setTextColor(ContextCompat.getColor(this, R.color.gray))
                    }
                    selectedStartDate != null && selectedEndDate != null &&
                            currentDate.isAfter(selectedStartDate) && currentDate.isBefore(selectedEndDate) -> {
                        view.setBackgroundResource(R.drawable.range_gradient_yellow)
                        view.setTextColor(ContextCompat.getColor(this, R.color.black))
                    }
                    currentDate.isEqual(LocalDate.now()) -> {
                        view.setBackgroundResource(R.drawable.current_date_purple)
                        view.setTextColor(ContextCompat.getColor(this, R.color.black))
                    }
                    else -> {
                        view.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                        view.setTextColor(ContextCompat.getColor(this, R.color.black))
                    }
                }
            }
        }
    }
}
