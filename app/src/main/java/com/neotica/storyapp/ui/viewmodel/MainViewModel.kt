package com.neotica.storyapp.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.models.LoginPreferences
import com.neotica.storyapp.retrofit.ApiService
import com.neotica.storyapp.ui.response.ResponseStatus
import com.neotica.storyapp.ui.response.Story
import kotlinx.coroutines.launch

class MainViewModel(
    private val apiService: ApiService,
    private val pref: LoginPreferences,
    application: Application
) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext.applicationContext
    private val _stories = MutableLiveData<ApiResult<List<Story>>>()
    val stories : LiveData<ApiResult<List<Story>>> = _stories

    init {
        getStory()
    }

/*    fun loadStories() {
        _stories.value = ApiResult.Loading
        viewModelScope.launch {
            try {
                val stories = repository.getStories()
                _stories.value = ApiResult.Success(stories)
            } catch (e: Exception) {
                _stories.value = ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }*/

    private fun getStory(){
        viewModelScope.launch {
            _stories.value = ApiResult.Loading
            val token = LoginPreferences(context).getToken()
            try {
                val response = apiService.getStory(token = "Bearer $token")
                if (response.isSuccessful){
                    val result = response.body()
                    if (result != null) {
                        _stories.value = ApiResult.Success(result.listStory)
                    }
                }else{
                    _stories.value = ApiResult.Error("Error: ${response.code()}")
                }
            }catch (e:Exception){
                _stories.value = ApiResult.Error(e.message.toString())
            }
        }
    }
}