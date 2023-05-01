package com.neotica.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.retrofit.ApiService
import com.neotica.storyapp.retrofit.response.story.ResponseStatus
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val apiService: ApiService) : ViewModel() {
    private val _responseUpload = MutableLiveData<ApiResult<ResponseStatus.ResponseStatusInner>>()
    val responseUpload: LiveData<ApiResult<ResponseStatus.ResponseStatusInner>> = _responseUpload

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            _responseUpload.value = ApiResult.Loading
            try {
                val response = apiService.uploadImage(token, file, description)
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _responseUpload.value = ApiResult.Success(result.res)
                    }
                } else {
                    _responseUpload.value = ApiResult.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _responseUpload.value = ApiResult.Error(e.message.toString())
            }
        }
    }
}