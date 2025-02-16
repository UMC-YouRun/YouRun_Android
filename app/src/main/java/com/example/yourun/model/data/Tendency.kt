package com.example.yourun.model.data

import com.google.gson.annotations.SerializedName

enum class Tendency(val value: String) {
    @SerializedName("페이스메이커") PACEMAKER("페이스메이커"),
    @SerializedName("스프린터") SPRINTER("스프린터"),
    @SerializedName("트레일러너") TRAIL_RUNNER("트레일러너");

    companion object {
        fun fromValue(value: String): Tendency {
            return values().firstOrNull { it.value == value } ?: PACEMAKER
        }
    }
}