package com.example.yourun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NextButtonViewModel : ViewModel() {
    private val _buttonText = MutableLiveData("다음")
    val buttonText: LiveData<String> get() = _buttonText

    private var currentPage = 0
    private var totalPages = 0

    fun initialize(totalPages: Int) {
        this.totalPages = totalPages
    }

    fun onButtonClick() {
        currentPage++
        if (currentPage >= totalPages) {
            // 마지막 페이지인 경우
            _buttonText.value = "완료"
        } else {
            // 다음 페이지로 이동
            _buttonText.value = "다음"
        }
    }
}
