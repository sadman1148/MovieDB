package com.techetronventures.moviedb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techetronventures.moviedb.data.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}