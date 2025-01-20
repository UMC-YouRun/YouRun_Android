package com.example.yourun.model.data

data class Question(
    val id: Int,
    val text: String,
    val detail: String,
    val answers: List<String>,
    var selectedAnswer: String? = null
)
