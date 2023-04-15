package com.neotica.storyapp.di

import com.neotica.storyapp.models.LoginPreferences
import com.neotica.storyapp.ui.viewmodel.AddStoryViewModel
import com.neotica.storyapp.ui.viewmodel.LoginViewModel
import com.neotica.storyapp.ui.viewmodel.MainViewModel
import com.neotica.storyapp.ui.viewmodel.RegisterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val registerViewModule = module { viewModel { RegisterViewModel(get()) } }

val loginViewModule = module {
    viewModel { LoginViewModel(get()) }
}

val mainViewModule = module {
    single { LoginPreferences(get()) }
    viewModel { MainViewModel(get(),get(), get()) }
}

val addStoryViewModule = module {
    viewModel { AddStoryViewModel(get()) }
}

val dataStoreModule = module {
    single { LoginPreferences(androidContext()) }
}