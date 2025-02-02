package com.example.yourun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuestionViewModel : ViewModel() {
    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    fun addScore(points: Int) {
        _score.value = (_score.value ?: 0) + points
    }

    fun resetScore() {
        _score.value = 0
    }
}
