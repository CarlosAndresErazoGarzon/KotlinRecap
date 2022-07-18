package com.example.taller2.db

import androidx.room.*
import com.example.taller2.model.Movie
import com.example.taller2.movie_list.popular.PopularMovie

@Dao
interface MovieDao {

    @Query("SELECT * FROM popularmovie")
    fun getAll(): List<PopularMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movie: PopularMovie)

    @Delete
    fun delete(movie: PopularMovie)
}