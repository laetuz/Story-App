package com.neotica.storyapp.ui.viewmodel.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.retrofit.ApiService
import com.neotica.storyapp.retrofit.response.story.Story
import kotlinx.coroutines.Dispatchers

class MapsRepository(
    private val apiService: ApiService
) {
    fun getStoriesMap(token: String): LiveData<ApiResult<List<Story>>> =
        liveData(Dispatchers.IO) {
            emit(ApiResult.Loading)
            try {
                val response = apiService.getStoryMap(token)
                if (response.isSuccessful) {
                    val stories = response.body()
                    stories?.let {
                        emit(ApiResult.Success(it.listStory))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message.toString()))
            }
        }
}