package com.example.taller2.movie_list.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.example.taller2.db.AppDatabase
import com.example.taller2.model.Movie
import com.example.taller2.MovieApplication
import com.example.taller2.movie_list.popular.PopularMovie
import com.example.taller2.movie_list.repository.GlobalState
import kotlinx.coroutines.launch

class FavListViewModel(application: Application) : AndroidViewModel(application) {

    private val movieRepository = (getApplication<Application>().applicationContext as MovieApplication).moviesRepository
    private val _favMoviesState = MutableLiveData<FavoriteState>(FavoriteState.Loading)
    val favMoviesState: LiveData<FavoriteState> = _favMoviesState

    init {
        observeRepository()
    }

    private fun observeRepository() {
        viewModelScope.launch {
            movieRepository.favMoviesGlobalState.collect {
                when (it) {
                    is GlobalState.Error -> _favMoviesState.value = FavoriteState.Error("Error")
                    is GlobalState.Success ->  _favMoviesState.value=
                        FavoriteState.Success(it.movies)
                    is GlobalState.Loading -> Log.d("POPULARLISTVIEWMODEL", "LOADING")
                }
            }
        }
    }

    fun onDelClicked(movie: PopularMovie) {
        movieRepository.removeFromFav(movie, getDB())
    }

    fun getDB(): AppDatabase {
        return Room.databaseBuilder(
            getApplication<Application>().applicationContext,
            AppDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()
    }

}

sealed class FavoriteState{
    data class Success(val movies: List<PopularMovie>) : FavoriteState()
    data class Error(val message: String) : FavoriteState()
    object Loading : FavoriteState()
}