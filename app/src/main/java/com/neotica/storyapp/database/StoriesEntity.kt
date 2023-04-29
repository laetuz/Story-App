package com.neotica.storyapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories_entity")
class StoriesEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = false)
    val id: String,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "description")
    val description: String,

    @field:ColumnInfo(name = "photoUrl")
    val photoUrl: String,

    @field:ColumnInfo(name = "lat")
    val lat: Double,

    @field:ColumnInfo(name = "lon")
    val lon: Double,

    @field:ColumnInfo(name = "createdAt")
    val createdAt: String
)