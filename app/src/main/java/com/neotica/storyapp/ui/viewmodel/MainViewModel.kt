package com.neotica.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.ui.response.Story

class MainViewModel(
    private val mainRepo: MainRepository
) : ViewModel() {

    fun getStories(): LiveData<ApiResult<List<Story>>> {
        return liveData {
            emit(ApiResult.Loading)
            emitSource(mainRepo.getStories())
        }
    }


/*    fun getStoryWithLocation(
        token: String,
        location: Int?
    ) = flow<ApiResult<ServerResponse>> {
        emit(Resource.loading())
        val response = apiService.getAllStories(token, location = location)
        response.let {
            if (!it.error) emit(Resource.success(it))
            else emit(Resource.error(it.message))
        }
    }.catch {
        Log.d(TAG, "getAllStories: ${it.message}")
        emit(Resource.error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)*/
}