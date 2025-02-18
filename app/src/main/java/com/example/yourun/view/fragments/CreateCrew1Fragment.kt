package com.example.yourun.view.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.databinding.FragmentCreateCrew1Binding
import com.example.yourun.model.data.request.CreateCrewChallengeRequest
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.launch
import java.time.LocalDate

class CreateCrew1Fragment : Fragment(R.layout.fragment_create_crew1) {

    private lateinit var binding: FragmentCreateCrew1Binding
    private lateinit var weekCalendarGrid: LinearLayout
    private var selectedStartDate: LocalDate? = LocalDate.now().plusDays(1)
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
            val crewName = binding.etCrewName.text.toString()
            val crewMotto = binding.etCrew.text.toString()
            val startDate = selectedStartDate?.toString() ?: ""
            val endDate = selectedEndDate?.toString() ?: ""
            Log.d(
                "CreateCrew1Fragment",
                "버튼 클릭됨. 크루명: $crewName, 슬로건: $crewMotto, 시작 날짜: $startDate, 종료 날짜: $endDate"
            )
            if (crewName.isBlank() || endDate.isBlank()) {
                Log.e("CreateCrew1Fragment", "입력값 부족: 크루명 또는 종료 날짜가 비어 있음")
                return@setOnClickListener
            }
            val request = CreateCrewChallengeRequest(
                crewName = crewName,
                endDate = endDate,
                slogan = crewMotto
            )

            lifecycleScope.launch {
                try {
                    Log.d("CreateCrew1Fragment", "API 요청 시작: $request")
                    val response = ApiClient.getApiService().createcrewchallenge(request)
                    if (response.status == 200 && response.data != null) {
                        Log.d("CreateCrew1Fragment", "API 응답 성공: ${response.data},${response.message}")

                        val crewName = response.data.crewName
                        val startDate = response.data.startDate
                        val endDate = response.data.endDate
                        val challengePeriod = response.data.challengePeriod
                        val tendency = response.data.tendency

                        val bundle = Bundle().apply {
                            putString("crewName", crewName)
                            putString("crewMotto", crewMotto)
                            putString("startDate", startDate)
                            putString("endDate", endDate)
                            putInt("challengePeriod", challengePeriod)
                            putString("tendency", tendency)
                        }
                        val fragment = CreateCrew2Fragment().apply { arguments = bundle }

                        Log.d("CreateCrew1Fragment", "다음 프래그먼트로 이동: CreateCrew2Fragment")
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.create_crew_fragment_container, fragment).commit()
                    } else {
                        Log.e("CreateCrew1Fragment", "API 응답 실패: ${response.code}, 데이터 없음")
                    }
                } catch (e: Exception) {
                    Log.e("CreateCrew1Fragment", "API 요청 중 오류 발생", e)
                }
            }
        }


        weekCalendarGrid = binding.calender
        setupWeekCalendar()
        val monthYearText: TextView = binding.monthYearText
        updateMonthYearText(monthYearText)
        highlightSelectedDates() }

    private fun updateMonthYearText(monthYearText: TextView) { val year = selectedDate.year
        val month = selectedDate.monthValue
        monthYearText.text = String.format("%d년 %d월", year, month) }

    private fun setupWeekCalendar() {
            val weekDates = getCurrentWeekDates(selectedDate)
            val weekdays = listOf("월", "화", "수", "목", "금", "토", "일")

            val weekdayLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            weekCalendarGrid.addView(weekdayLayout)

            weekDates.forEachIndexed { index, date ->
                val weekdayIndex = (date.dayOfWeek.value - 1) % 7
                val dayView = TextView(requireContext()).apply {
                    text = weekdays[weekdayIndex]
                    textSize = 14f
                    gravity = Gravity.CENTER
                    setTypeface(null, Typeface.BOLD)
                }
                val layoutParams =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                dayView.layoutParams = layoutParams
                weekdayLayout.addView(dayView)
            }

            val dateLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            weekCalendarGrid.addView(dateLayout)

            weekDates.forEach { date ->
                val dateView = createDateView(date.dayOfMonth, date)
                dateLayout.addView(dateView)
            }
        }

        private fun createDateView(day: Int, currentDate: LocalDate): TextView {
            val textView = TextView(requireContext())
            val totalWidth = resources.displayMetrics.widthPixels
            val margin = 2
            val cellSize = (totalWidth - margin * 14) / 7
            val layoutParams = LinearLayout.LayoutParams(cellSize, cellSize)
            layoutParams.setMargins(margin, margin, margin, margin)
            textView.layoutParams = layoutParams
            textView.text = day.toString()
            textView.textSize = 16f
            textView.gravity = Gravity.CENTER
            textView.isClickable = true
            textView.isFocusable = true
            textView.tag = currentDate.toString()

            textView.setOnClickListener {
                if (selectedStartDate == null || (selectedEndDate != null)) {
                    selectedStartDate = currentDate
                    selectedEndDate = null
                } else {
                    selectedEndDate = currentDate
                    if (selectedStartDate!!.isAfter(selectedEndDate)) {
                        selectedStartDate =
                            selectedEndDate.also { selectedEndDate = selectedStartDate }
                    }
                }
                highlightSelectedDates()
            }

            highlightSelectedDay(currentDate, textView)
            return textView
        }


    private fun highlightSelectedDay(currentDate: LocalDate, textView: TextView) {
        when {
            currentDate.isEqual(selectedStartDate) -> {
                textView.setBackgroundResource(R.drawable.bgd_selected_date_orange)
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }

            currentDate.isEqual(LocalDate.now()) -> {
                textView.setBackgroundResource(R.drawable.bgd_current_date_purple)
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }

            else -> {
                textView.setBackgroundColor(Color.TRANSPARENT)
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }

    }

    private fun highlightSelectedDates() {
        weekCalendarGrid.children.forEach { row ->
            if (row is LinearLayout) {
                row.children.forEach { view ->
                    if (view is TextView) {
                        val dateText = view.text.toString().toIntOrNull()
                        dateText?.let {
                            val currentDate = selectedDate.withDayOfMonth(dateText)

                            when {
                                // 시작 날짜 & 마지막 날짜
                                selectedStartDate != null && currentDate.isEqual(selectedStartDate) ||
                                        selectedEndDate != null && currentDate.isEqual(selectedEndDate) -> {
                                    view.setBackgroundResource(R.drawable.bgd_selected_date_orange)
                                    view.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                                }

                                // 중간 날짜 (연한 주황색)
                                selectedStartDate != null && selectedEndDate != null &&
                                        currentDate.isAfter(selectedStartDate) && currentDate.isBefore(selectedEndDate) -> {
                                    view.setBackgroundResource(R.drawable.bgd_middle_date)
                                    view.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                                }

                                // 오늘 날짜
                                currentDate.isEqual(LocalDate.now()) -> {
                                    view.setBackgroundResource(R.drawable.bgd_current_date_purple)
                                    view.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                                }

                                else -> {
                                    view.setBackgroundColor(Color.TRANSPARENT)
                                    view.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                                }
                            }
                        }
                    }
                }
            }
        }
        }
                private fun getCurrentWeekDates(date: LocalDate): List<LocalDate> {
                    return (0..6).map { date.plusDays(it.toLong()) }
                }
            }

