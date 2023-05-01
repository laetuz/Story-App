package com.neotica.storyapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neotica.storyapp.retrofit.response.auth.Register
import com.neotica.storyapp.retrofit.ApiService
import com.neotica.storyapp.retrofit.RegisterResponse
import com.neotica.storyapp.retrofit.response.story.Story
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(
    private val apiService: ApiService
) : ViewModel() {
    private val _story = MutableLiveData<List<Story>>()
    val story: LiveData<List<Story>> = _story
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    fun signup(name: String, email: String, pass: String) {
        _isLoading.value = true
        val registerInfo = Register(name, email, pass)
        apiService.register(registerInfo).enqueue(object :
            Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _success.value = false
                Log.d("regLog", "failed to register.")
            }

            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.code() == 201) {
                    _isLoading.value = false
                    _success.value = true
                    Log.d("regLog", "registered.")

                } else {
                    _isLoading.value = false
                    _success.value = false
                    Log.d("regLog", "response came but not registered.")
                }
            }
        })
    }
}