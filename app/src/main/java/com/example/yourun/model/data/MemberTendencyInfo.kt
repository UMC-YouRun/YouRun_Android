package com.example.yourun.model.data

import com.example.yourun.utils.TendencyAdapter
import com.google.gson.annotations.JsonAdapter

data class MemberTendencyInfo(
    val userId: Long,
    @JsonAdapter(TendencyAdapter::class)
    val memberTendency: String
)
