package com.example.yourun.model.data

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    val status: Int,
    val code: String?,
    val message: String?,
    @SerializedName("data") val data: T?
)
