package com.captvelsky.storyapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [StoryResponseItem::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}