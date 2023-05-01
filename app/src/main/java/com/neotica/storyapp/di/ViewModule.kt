package com.neotica.storyapp.di

import com.neotica.storyapp.ui.viewmodel.AddStoryViewModel
import com.neotica.storyapp.ui.viewmodel.LoginViewModel
import com.neotica.storyapp.ui.viewmodel.repository.MainRepository
import com.neotica.storyapp.ui.viewmodel.MainViewModel
import com.neotica.storyapp.ui.viewmodel.repository.MapsRepository
import com.neotica.storyapp.ui.viewmodel.MapsViewModel
import com.neotica.storyapp.ui.viewmodel.RegisterViewModel
import com.neotica.storyapp.ui.viewmodel.repository.LoginRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MapsViewModel(get()) }
    viewModel { AddStoryViewModel(get()) }
    viewModel { MainViewModel(get()) }
}

val repoModule = module {
    single { MainRepository(get(), get()) }
    single { MapsRepository(get()) }
    single { LoginRepository(get()) }
}