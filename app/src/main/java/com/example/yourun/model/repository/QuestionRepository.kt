package com.example.yourun.model.repository

import com.example.yourun.model.data.response.Question

object QuestionRepository {
    val questions = listOf(
        Question(1, "오늘은 꼭 운동을 해야지!\n" + "어느 시간에 나갈 예정인가요?", "둘 중 하나를 선택해주세요 :)", listOf("하루의 시작을 상쾌하게! 아침 7시", "운동은 역시 하루의 끝에! 저녁 9시")),
        Question(2, "운동하기 싫은데...\n" + "그치만 이럴 때 의지가 불타올라!", "둘 중 하나를 선택해주세요 :)", listOf("라이벌에게 질 수 없다! 경쟁할 때", "다 함께 으쌰으쌰! 협동할 때")),
        Question(3, "러닝 목표를 세울 때\n" + "더 중요하다고 여기는 것은?", "둘 중 하나를 선택해주세요 :)", listOf("짧고 굵게! 목표 시간 채우기", "가능한 길게! 목표 거리 채우기")),
        Question(4, "토끼와 거북이의 경주!\n" + "당신이라면 어떤 동물일까요?", "둘 중 하나를 선택해주세요 :)", listOf("총알처럼 재빠르게! 토끼", "한 발 한 발 꾸준하게! 거북이"))
    )
}