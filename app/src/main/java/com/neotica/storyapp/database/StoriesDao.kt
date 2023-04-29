package com.neotica.storyapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoriesDao {
    @Query("SELECT * FROM stories_entity")
    fun getStories(): LiveData<List<StoriesEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStory(stories: List<StoriesEntity>)

    @Query("DELETE FROM stories_entity")
    suspend fun deleteStories()
}
