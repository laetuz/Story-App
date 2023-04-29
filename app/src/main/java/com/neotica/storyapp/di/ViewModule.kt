package com.neotica.storyapp.di

import com.neotica.storyapp.models.LoginPreferences
import com.neotica.storyapp.ui.viewmodel.AddStoryViewModel
import com.neotica.storyapp.ui.viewmodel.LoginViewModel
import com.neotica.storyapp.ui.viewmodel.MainRepository
import com.neotica.storyapp.ui.viewmodel.MainViewModel
import com.neotica.storyapp.ui.viewmodel.MapsRepository
import com.neotica.storyapp.ui.viewmodel.MapsViewModel
import com.neotica.storyapp.ui.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val registerViewModule = module { viewModel { RegisterViewModel(get()) } }

val loginViewModule = module {
    viewModel { LoginViewModel(get()) }
}

val mainViewModule = module {
    single { LoginPreferences(get()) }
    viewModel { MainViewModel(get()) }
}

val addStoryViewModule = module {
    viewModel { AddStoryViewModel(get()) }
}

val mainRepo = module {
    single { MainRepository(get(),get()) }
}

val mapsCombo = module {
    single { MapsRepository(get(),get()) }
    viewModel { MapsViewModel(get()) }
}