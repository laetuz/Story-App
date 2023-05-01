package com.neotica.storyapp.retrofit

import com.google.gson.annotations.SerializedName
import com.neotica.storyapp.retrofit.response.auth.User
import com.neotica.storyapp.retrofit.response.auth.Register
import com.neotica.storyapp.retrofit.response.auth.UserLogin
import com.neotica.storyapp.retrofit.response.story.ResponseStatus
import com.neotica.storyapp.retrofit.response.story.ResponseStories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("v1/stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page : Int? = null,
        @Query("size") size: Int? = null,
    ): Response<ResponseStories>

    @GET("v1/stories")
    suspend fun getStoryMap(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): Response<ResponseStories>

    @POST("v1/login")
    fun login(
        @Body user: User
    ): Call<LoginResponse>

    @POST("v1/register")
    fun register(
        @Body info: Register
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