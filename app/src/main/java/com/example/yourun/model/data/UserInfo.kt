package com.example.yourun.model.data


data class UserInfo(
    val id : Long,
    val nickname : String,
    val tendency : String,
    val tags : List<String>,
    val crewReward : Long,
    val personalReward : Long,
    val mvp : Long
)