package com.neotica.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotica.storyapp.utils.LoginPreferences
import com.neotica.storyapp.utils.UserLogin
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: LoginPreferences) : ViewModel() {
    fun login(){
        viewModelScope.launch {
            pref.login()
        }
    }
    fun saveUser(login: UserLogin){
        viewModelScope.launch { pref.saveUser(login) }
    }
}