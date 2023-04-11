package com.neotica.storyapp.di

import com.neotica.storyapp.ui.viewmodel.LoginViewModel
import com.neotica.storyapp.ui.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val registerViewModule = module {
    viewModel { RegisterViewModel(get()) }
}

val loginViewModule = module { viewModel { LoginViewModel(get()) } }