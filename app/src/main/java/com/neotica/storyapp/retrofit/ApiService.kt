package com.neotica.storyapp.retrofit

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.neotica.storyapp.response.user.User
import com.neotica.storyapp.response.user.UserBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("v1/login")
    fun login(
        @Body user: User
    ): Call<LoginResponse>

    @POST("v1/register")
    fun register(
        @Body info: UserBody
    ): Call<RegisterResponse>
}

data class RegisterResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)

data class LoginResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: JsonObject
)