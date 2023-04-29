package com.neotica.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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

    suspend fun getMap(): LiveData<List<Story>> {
        return Transformations.map(mainRepo.getStories()) { apiResult ->
            when(apiResult) {
                is ApiResult.Success -> apiResult.data
                else -> emptyList() // or handle other error cases
            }
        }
    }

    fun getStoryPaging(): LiveData<PagingData<Story>> = mainRepo.getStoriesPaging().cachedIn(viewModelScope)

}