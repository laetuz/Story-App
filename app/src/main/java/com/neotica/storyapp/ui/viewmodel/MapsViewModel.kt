package com.neotica.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.neotica.storyapp.ui.viewmodel.repository.MapsRepository

class MapsViewModel(
    private val mapsRepo: MapsRepository
) : ViewModel() {

    fun getCompletedStories(token: String) = mapsRepo.getStoriesMap(token)

}