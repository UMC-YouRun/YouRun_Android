package com.example.yourun.view.activities
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.yourun.R
import com.example.yourun.databinding.ActivityCreateCrew1Binding
import java.time.LocalDate



class CreateCrew1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCrew1Binding
    private lateinit var weekCalendarGrid: GridLayout
    private var selectedStartDate: LocalDate? = null
    private var selectedEndDate: LocalDate? = null
    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCrew1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        weekCalendarGrid = findViewById(R.id.weekCalendarGrid)
        setupWeekCalendar()

        val monthYearText: TextView = findViewById(R.id.monthYearText)
        updateMonthYearText(monthYearText)
    }

    private fun updateMonthYearText(monthYearText: TextView) {
        val year = selectedDate.year
        val month = selectedDate.monthValue
        monthYearText.text = String.format("%d년 %d월", year, month)
    }

    private fun setupWeekCalendar() {
        val weekDates = getCurrentWeekDates(selectedDate)
        val weekdays = listOf("월", "화", "수", "목", "금", "토", "일")

        weekCalendarGrid.removeAllViews()

        // 요일 표시
        weekDates.forEach { date ->
            val weekdayIndex = date.dayOfWeek.value % 7
            val dayView = TextView(this).apply {
                text = weekdays[weekdayIndex]
                textSize = 14f
                gravity = Gravity.CENTER
                setTypeface(null, Typeface.BOLD)
            }
            val layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            dayView.layoutParams = layoutParams
            weekCalendarGrid.addView(dayView)
        }

        // 날짜 표시
        weekDates.forEach { date ->
            val weekdayIndex = date.dayOfWeek.value % 7
            val dateView = createDateView(date.dayOfMonth, date)
            weekCalendarGrid.addView(dateView)
        }
    }

    private fun createDateView(day: Int, currentDate: LocalDate): TextView {
        val textView = TextView(this)
        val layoutParams = GridLayout.LayoutParams()
        val totalWidth = resources.displayMetrics.widthPixels
        val margin = 4
        val cellSize = (totalWidth - margin * 14) / 7

        layoutParams.setMargins(margin, margin, margin, margin)
        layoutParams.width = cellSize
        layoutParams.height = cellSize
        textView.layoutParams = layoutParams
        textView.text = day.toString()
        textView.textSize = 16f
        textView.gravity = Gravity.CENTER

        textView.setOnClickListener {
            if (selectedStartDate == null || (selectedStartDate != null && selectedEndDate != null)) {
                selectedStartDate = currentDate
                selectedEndDate = null
            } else {
                selectedEndDate = currentDate
                if (selectedStartDate!!.isAfter(selectedEndDate)) {
                    selectedStartDate = selectedEndDate.also { selectedEndDate = selectedStartDate }
                }
            }
            highlightSelectedDates()
        }
        highlightSelectedDay(currentDate, textView)
        return textView
    }

    private fun highlightSelectedDay(currentDate: LocalDate, textView: TextView) {
        when {
            currentDate.isEqual(LocalDate.now()) -> {
                textView.setBackgroundResource(R.drawable.current_date_purple)
                textView.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
            else -> {
                textView.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                textView.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }
    }

    private fun highlightSelectedDates() {
        weekCalendarGrid.children.forEach { view ->
            if (view is TextView) {
                val dayText = view.text.toString().toInt()
                val currentDate = selectedDate.withDayOfMonth(dayText)
                when {
                    selectedStartDate != null && currentDate.isEqual(selectedStartDate) -> {
                        view.setBackgroundResource(R.drawable.selected_date_orange)
                        view.setTextColor(ContextCompat.getColor(this, R.color.white))
                    }
                    selectedEndDate != null && currentDate.isEqual(selectedEndDate) -> {
                        view.setBackgroundResource(R.drawable.selected_date_orange)
                        view.setTextColor(ContextCompat.getColor(this, R.color.white))
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

    private fun getCurrentWeekDates(date: LocalDate): List<LocalDate> {
        val todayDayOfWeek = date.dayOfWeek.value
        val startOfWeek = date.minusDays((todayDayOfWeek - 1).toLong())
        return (0 until 7).map { startOfWeek.plusDays(it.toLong()) }
    }
}
