package com.example.taller2.movie_list.popular

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taller2.model.Movie

@Entity
data class PopularMovie(
    @PrimaryKey val id: String,
    @ColumnInfo (name= "title")val title: String,
    @ColumnInfo (name= "popularity")val popularity: String,
    @ColumnInfo (name= "vote")val vote: Int,
    @ColumnInfo (name= "backdrop_path")val backdrop_path: String,
    @ColumnInfo (name= "isFavorite")var isFavorite: Boolean
){
    fun toMovie():Movie{
        return Movie(this.id, this.title, this.popularity, this.vote, this.backdrop_path)
    }
}
