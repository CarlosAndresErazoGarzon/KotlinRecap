package com.example.taller2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taller2.movie_list.popular.PopularMovie

@Entity
data class Movie(
    @PrimaryKey val id: String,
    @ColumnInfo (name= "title")val title: String,
    @ColumnInfo (name= "popularity")val popularity: String,
    @ColumnInfo (name= "vote")val vote: Int,
    @ColumnInfo (name= "poster_path")val backdrop_path: String
    ) {
}