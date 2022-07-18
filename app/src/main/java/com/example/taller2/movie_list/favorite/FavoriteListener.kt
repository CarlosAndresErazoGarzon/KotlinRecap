package com.example.taller2.movie_list.favorite

import com.example.taller2.model.Movie
import com.example.taller2.movie_list.popular.PopularMovie

interface FavoriteListener {
    fun onAddedFavorite(movie: PopularMovie)
    fun onRemoveFavorite(movie: PopularMovie)
}