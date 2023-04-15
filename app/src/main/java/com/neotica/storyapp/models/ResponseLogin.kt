package com.neotica.storyapp.models


import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("loginResult")
    val loginResult: UserLogin,
    @SerializedName("message")
    val message: String
)