package com.neotica.storyapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neotica.storyapp.ui.response.Story

/*@Dao
interface StoryDao {
    @Query("SELECT * FROM story_entity")
    fun getStories(): PagingSource<Int, Story>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(user_stories: List<Story>):Long

    @Query("DELETE FROM story_entity")
    suspend fun deleteStories(): Int
}*/






