package com.neotica.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel

class MapsViewModel(
    private val mapsRepo: MapsRepository
) : ViewModel() {

    fun getCompletedStories(token: String, location: Int) = mapsRepo.getStoriesMap(token, location)

}