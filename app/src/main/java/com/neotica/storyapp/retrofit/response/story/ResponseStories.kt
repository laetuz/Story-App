package com.neotica.storyapp.retrofit.response.story


import com.google.gson.annotations.SerializedName

data class ResponseStories(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: List<Story>,
    @SerializedName("message")
    val message: String
)