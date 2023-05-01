package com.neotica.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.retrofit.response.story.Story
import com.neotica.storyapp.ui.viewmodel.repository.MainRepository

class MainViewModel(
    private val mainRepo: MainRepository
) : ViewModel() {

    fun getStories(): LiveData<ApiResult<List<Story>>> {
        return liveData {
            emit(ApiResult.Loading)
            emitSource(mainRepo.getStories())
        }
    }

    fun getStoryPaging(): LiveData<PagingData<Story>> = mainRepo.getStoriesPaging().cachedIn(viewModelScope)

}