package com.techetronventures.moviedb.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.techetronventures.moviedb.data.model.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: Int): Movie?

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    suspend fun getMoviesSortedByPopularity(): List<Movie>

    @Query("SELECT COUNT(*) FROM movies WHERE id = :movieId")
    suspend fun isMovieInDatabase(movieId: Int): Boolean

    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteById(movieId: Int)
}