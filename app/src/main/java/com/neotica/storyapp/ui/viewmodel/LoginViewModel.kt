package com.neotica.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.retrofit.response.auth.UserLogin
import com.neotica.storyapp.ui.viewmodel.repository.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val _responseLogin = loginRepository.responseLogin
    val responseLogin: LiveData<ApiResult<UserLogin>> = _responseLogin

    fun loginUser(email: String, password: String) {
        loginRepository.loginUser(email, password)
    }
}