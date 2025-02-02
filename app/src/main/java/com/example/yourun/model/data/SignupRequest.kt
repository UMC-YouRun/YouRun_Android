package com.example.yourun.model.data

// 회원가입 Step 1 요청 (이메일 & 비밀번호)
data class SignUpRequest1(
    val email: String,
    val password: String
)

// 회원가입 Step 3 요청 (닉네임 & 성향 태그)
data class SignUpRequest3(
    val nickname: String,
    val tag1 : String,
    val tag2 : String
)

// 회원가입 응답
data class SignUpResponse(
    val id: String,
    val email: String,
    val nickname: String,
    val tags: List<String>
)

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null
)