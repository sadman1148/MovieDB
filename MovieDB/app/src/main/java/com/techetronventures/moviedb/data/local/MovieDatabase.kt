package com.techetronventures.moviedb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.techetronventures.moviedb.data.model.Movie
import com.techetronventures.moviedb.utils.Converters

@Database(entities = [Movie::class], version = 4)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}