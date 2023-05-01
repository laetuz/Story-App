package com.neotica.storyapp.ui.viewmodel.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.neotica.storyapp.models.PagingSource
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.models.LoginPreferences
import com.neotica.storyapp.retrofit.ApiService
import com.neotica.storyapp.retrofit.response.story.Story

class MainRepository(
    private val apiService: ApiService,
    private val context: Context
) {
    fun getStoriesPaging(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PagingSource(apiService, context)
            }
        ).liveData
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

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