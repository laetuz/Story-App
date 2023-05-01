package com.neotica.storyapp.retrofit.response.auth

import com.google.gson.annotations.SerializedName


data class UserLogin(
    @SerializedName("name")
    val name: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("userId")
    val userId: String
)