package com.neotica.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.neotica.storyapp.database.StoriesEntity
import com.neotica.storyapp.database.StoriesDao
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.retrofit.ApiService
import kotlinx.coroutines.Dispatchers

class MapsRepository(
    private val apiService: ApiService,
    private val dao: StoriesDao
) {


    fun getStoriesMap(token: String, location: Int): LiveData<ApiResult<List<StoriesEntity>>> = liveData(Dispatchers.IO) {
        emit(ApiResult.Loading)
        try {
            val response = apiService.getStoryMap("Bearer $token", location)
            val stories = response.body()?.listStory
            val listStories = stories
                ?.map { story ->
                StoriesEntity(
                    story.id,
                    story.name,
                    story.description,
                    story.photoUrl,
                    story.lat,
                    story.lon,
                    story.createdAt
                )
            }
            dao.deleteStories()
            if (listStories != null) {
                dao.insertStory(listStories)
            }
        } catch (e: Exception) {
            //Log.d("StoriesRepository", "getStories: ${e.message.toString()} ")
            emit(ApiResult.Error(e.message.toString()))
        }
        val localData: LiveData<ApiResult<List<StoriesEntity>>> =
            dao.getStories().map {
                ApiResult.Success(it)
            }
        emitSource(localData)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

}