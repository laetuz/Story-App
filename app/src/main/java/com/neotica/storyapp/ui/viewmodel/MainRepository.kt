package com.neotica.storyapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.models.LoginPreferences
import com.neotica.storyapp.retrofit.ApiService
import com.neotica.storyapp.ui.response.Story

class MainRepository(
    private val apiService: ApiService,
    private val context: Context
) {
    suspend fun getStories(): LiveData<ApiResult<List<Story>>> {
        val result = MutableLiveData<ApiResult<List<Story>>>()
        result.value = ApiResult.Loading
        val token = LoginPreferences(context).getToken()

        try {
            val response = apiService.getStory("Bearer $token")
            if (response.isSuccessful) {
                val stories = response.body()?.listStory ?: emptyList()
                result.value = ApiResult.Success(stories)
            } else {
                result.value = ApiResult.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            result.value = ApiResult.Error(e.message.toString())
        }

        return result
    }
}