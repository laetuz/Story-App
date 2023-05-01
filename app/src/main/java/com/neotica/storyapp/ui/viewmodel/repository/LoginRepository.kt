package com.neotica.storyapp.ui.viewmodel.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.retrofit.response.auth.UserLogin
import com.neotica.storyapp.retrofit.response.auth.User
import com.neotica.storyapp.retrofit.ApiService
import com.neotica.storyapp.retrofit.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(private val apiService: ApiService) {
    private val _responseLogin = MutableLiveData<ApiResult<UserLogin>>()
    val responseLogin: LiveData<ApiResult<UserLogin>> = _responseLogin
    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    fun loginUser(email: String, password: String) {
        val loginInfo = User(email, password)
        isLoading.value = true
        apiService.login(loginInfo).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val result = response.body()
                isLoading.value = false
                if (result != null) {
                    _responseLogin.value = ApiResult.Success(result.loginResult)
                    Log.d("neotica", "Response succeed")
                } else {
                    _responseLogin.value = ApiResult.Error("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isLoading.value = false
                Log.d("neotica", "Error taking responds.")
            }
        })
    }
}