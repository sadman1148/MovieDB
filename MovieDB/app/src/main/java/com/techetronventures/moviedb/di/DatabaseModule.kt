package com.techetronventures.moviedb.di

import android.content.Context
import androidx.room.Room
import com.techetronventures.moviedb.data.local.MovieDao
import com.techetronventures.moviedb.data.local.MovieDatabase
import com.techetronventures.moviedb.utils.Constants
import com.techetronventures.moviedb.utils.Converters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java, Constants.LOCAL_DB_NAME)
            .fallbackToDestructiveMigration()
            .addTypeConverter(Converters())
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(db: MovieDatabase): MovieDao {
        return db.movieDao()
    }
}