package com.example.yourun.model.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val status: Int,
    val code: String?,
    val message: String,
    val data: TokenData?
)

data class TokenData(
    @SerializedName("access_token")
    val accessToken: String
)
