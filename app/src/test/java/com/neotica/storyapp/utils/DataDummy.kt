package com.neotica.storyapp.utils

import com.neotica.storyapp.retrofit.response.story.Story

object DataDummy {
    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "name + $i",
                "description $i",
                "photoUrl https://neotica.id/$i",
                "createdAt $i",
                40.7434,
                74.0080,
                )
            items.add(story)
        }
        return items
    }
}