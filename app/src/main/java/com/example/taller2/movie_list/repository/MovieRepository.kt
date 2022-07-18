package com.example.taller2.movie_list.repository

import android.util.Log
import com.example.taller2.db.AppDatabase
import com.example.taller2.model.Movie
import com.example.taller2.movie_list.popular.PopularMovie
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MovieRepository(
    private val externalScope: CoroutineScope,
) {

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("MOVIEREPOSITORY_ERROR","CoroutineExceptionHandler got $exception")
    }

    private val _favMoviesGlobalState = MutableStateFlow<GlobalState>(GlobalState.Success(emptyList()))
    val favMoviesGlobalState: StateFlow<GlobalState> = _favMoviesGlobalState

    private val _popMoviesGlobalState = MutableStateFlow<GlobalState>(GlobalState.Success(emptyList()))
    val popMoviesGlobalState: StateFlow<GlobalState> = _popMoviesGlobalState

    fun addFromFav(movie: PopularMovie, db: AppDatabase){
        externalScope.launch(exceptionHandler) {
            when (val currentState = _favMoviesGlobalState.value){
                is GlobalState.Success  -> {
                    _favMoviesGlobalState.value=
                        GlobalState.Success(mutableListOf<PopularMovie>().also {
                            it.addAll(currentState.movies)
                            it.add(movie)
                            withContext(Dispatchers.Default){
                                db.movieDao().insert(movie)
                            }
                        }
                    )
                }
                else -> {
                    _favMoviesGlobalState.value =
                        GlobalState.Success(mutableListOf<PopularMovie>().also {
                            it.add(movie)
                            withContext(Dispatchers.Default){
                                db.movieDao().insert(movie)
                            }
                        })

                }
            }
        }

    }

    fun removeFromFav(movie: PopularMovie, db: AppDatabase){
        externalScope.launch(exceptionHandler) {
            val currentState = _favMoviesGlobalState.value
            when (currentState){
                is GlobalState.Success  -> {
                    _favMoviesGlobalState.value =
                        GlobalState.Success(mutableListOf<PopularMovie>().also {
                            movie.isFavorite = false
                            it.addAll(currentState.movies)
                            it.remove(movie)
                            withContext(Dispatchers.Default){
                                db.movieDao().delete(movie)
                            }
                        })
                }
                is GlobalState.Loading -> Log.d("MOVIEREPOSITORY", "LOADING")
                else -> currentState
            }
        }
    }

    fun setPop(popList: MutableList<PopularMovie>){
        externalScope.launch(exceptionHandler) {
            _popMoviesGlobalState.emit(
                GlobalState.Success(popList)
            )
        }
    }
    fun setFav(favList: List<PopularMovie>){
        externalScope.launch(exceptionHandler) {
            _favMoviesGlobalState.emit(
                GlobalState.Success(favList)
            )
        }
    }
}

sealed class GlobalState{
    data class Success(val movies : List<PopularMovie>) : GlobalState(){
        init {
            Log.d("GLOBAL SUCCESS", movies.toString())
        }
    }
    data class Error(val message : String) : GlobalState()
    object Loading : GlobalState()
}