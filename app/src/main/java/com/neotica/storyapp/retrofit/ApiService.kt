package com.neotica.storyapp.retrofit

import com.google.gson.annotations.SerializedName
import com.neotica.storyapp.response.user.User
import com.neotica.storyapp.response.user.UserBody
import com.neotica.storyapp.response.user.UserLogin
import com.neotica.storyapp.ui.response.ResponseStatus
import com.neotica.storyapp.ui.response.ResponseStories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("v1/stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("location") location: Int? = null
    ): Response<ResponseStories>

    @POST("v1/login")
    fun login(
        @Body user: User
    ): Call<LoginResponse>

    @POST("v1/register")
    fun register(
        @Body info: UserBody
    ): Call<RegisterResponse>

    @Multipart
    @POST("v1/stories")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Response<ResponseStatus>

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
    val loginResult: UserLogin
)