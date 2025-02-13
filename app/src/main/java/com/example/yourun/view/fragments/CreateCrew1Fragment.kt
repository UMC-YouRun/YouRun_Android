package com.example.yourun.view.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.yourun.R
import com.example.yourun.databinding.FragmentCreateCrew1Binding
import java.time.LocalDate

class CreateCrew1Fragment : Fragment(R.layout.fragment_create_crew1) {

    private lateinit var binding: FragmentCreateCrew1Binding
    private lateinit var weekCalendarGrid: LinearLayout
    private var selectedStartDate: LocalDate? = null
    private var selectedEndDate: LocalDate? = null
    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 바인딩 초기화
        binding = FragmentCreateCrew1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.create_crew_fragment_container, CreateCrew2Fragment())
                .commit()
        }

        weekCalendarGrid = binding.calender ?: return
        setupWeekCalendar()

        val monthYearText: TextView = binding.monthYearText
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

        // 요일을 표시할 LinearLayout을 설정합니다.
        val weekdayLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        }
        weekCalendarGrid.addView(weekdayLayout)

        // 요일을 추가합니다.
        weekDates.forEachIndexed { index, date ->
            val weekdayIndex = (date.dayOfWeek.value - 1) % 7
            val dayView = TextView(requireContext()).apply {
                text = weekdays[weekdayIndex]
                textSize = 14f
                gravity = Gravity.CENTER
                setTypeface(null, Typeface.BOLD)
            }
            val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            dayView.layoutParams = layoutParams
            weekdayLayout.addView(dayView)
        }

        // 날짜를 표시할 LinearLayout을 설정합니다.
        val dateLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        }
        weekCalendarGrid.addView(dateLayout)

        // 날짜를 추가합니다.
        weekDates.forEach { date ->
            val dateView = createDateView(date.dayOfMonth, date)
            dateLayout.addView(dateView)
        }
    }

    private fun createDateView(day: Int, currentDate: LocalDate): TextView {
        val textView = TextView(requireContext())
        val totalWidth = resources.displayMetrics.widthPixels
        val margin = 2
        val cellSize = (totalWidth - margin * 14) / 7 // 셀 크기 조정

        val layoutParams = LinearLayout.LayoutParams(cellSize, cellSize)
        layoutParams.setMargins(margin, margin, margin, margin)
        textView.layoutParams = layoutParams
        textView.text = day.toString()
        textView.textSize = 16f
        textView.gravity = Gravity.CENTER

        textView.setClickable(true)
        textView.setFocusable(true)

        textView.setOnClickListener {
            // Toggle the selected dates logic
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

        // Highlight current date differently
        highlightSelectedDay(currentDate, textView)
        return textView
    }

    private fun highlightSelectedDay(currentDate: LocalDate, textView: TextView) {
        when {
            currentDate.isEqual(LocalDate.now()) -> {
                textView.setBackgroundResource(R.drawable.current_date_purple)
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            else -> {
                textView.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }
    }

    private fun highlightSelectedDates() {
        weekCalendarGrid.children.forEach { view ->
            if (view is TextView) {
                val dayText = view.text.toString().toIntOrNull()
                dayText?.let {
                    val currentDate = selectedDate.withDayOfMonth(dayText)
                    when {
                        selectedStartDate != null && currentDate.isEqual(selectedStartDate) -> {
                            view.setBackgroundResource(R.drawable.selected_date_orange)
                            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        }
                        selectedEndDate != null && currentDate.isEqual(selectedEndDate) -> {
                            view.setBackgroundResource(R.drawable.selected_date_orange)
                            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        }
                        selectedStartDate != null && selectedEndDate != null &&
                                currentDate.isAfter(selectedStartDate) && currentDate.isBefore(selectedEndDate) -> {
                            view.setBackgroundResource(R.drawable.range_gradient_yellow)
                            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        }
                        currentDate.isEqual(LocalDate.now()) -> {
                            view.setBackgroundResource(R.drawable.current_date_purple)
                            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        }
                        else -> {
                            view.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        }
                    }
                }
            }
        }
    }

    // 오늘부터 6일 후까지의 날짜 리스트 가져오기
    private fun getCurrentWeekDates(date: LocalDate): List<LocalDate> {
        return (0..6).map { date.plusDays(it.toLong()) }
    }


}
