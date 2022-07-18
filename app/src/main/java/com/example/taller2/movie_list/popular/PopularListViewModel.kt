package com.example.taller2.movie_list.popular

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.example.taller2.db.AppDatabase
import com.example.taller2.MovieApplication
import com.example.taller2.movie_list.repository.GlobalState
import com.example.taller2.network.APIservice
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SetListViewModel(application: Application) : AndroidViewModel(application){

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("POPULARLISTVIEWMODEL_ERROR","CoroutineExceptionHandler got $exception")
    }

    val mdbApi by lazy { getRetrofit().create(APIservice::class.java) }

    private val movieRepository = (getApplication<Application>().applicationContext as MovieApplication).moviesRepository

    private val _setListState = MutableLiveData<SetState>(SetState.Loading(""))
    val setListState : LiveData<SetState> = _setListState

    private var popularMovies: List<PopularMovie> = emptyList()
    private var favMovies: List<PopularMovie> = emptyList()

    init {
        loadMovies()
        observePopRepository()
        observeFavRepository()
    }

    private fun observePopRepository() {
        viewModelScope.launch {

            movieRepository.popMoviesGlobalState.collect {
                val currentState = _setListState.value
                when (it) {
                    is GlobalState.Error -> Log.d("POPULARLISTVIEWMODEL", "ERROR")
                    is GlobalState.Success ->
                        parseMovies(
                            it.movies,
                            when (currentState) {
                                is SetState.Success -> currentState.favMovies
                                else -> emptyList()
                            }
                        )
                    is GlobalState.Loading -> Log.d("POPULARLISTVIEWMODEL", "LOADING")
                }
            }
        }
    }

    private fun observeFavRepository() {
        viewModelScope.launch {
            movieRepository.favMoviesGlobalState.collect {
                val currentState = _setListState.value
                when (it) {
                    is GlobalState.Error -> Log.d("POPULARLISTVIEWMODEL", "ERROR")
                    is GlobalState.Success -> parseMovies(
                        when (currentState) {
                        is SetState.Success -> currentState.popularMovies
                        else -> emptyList()
                        },
                        it.movies)
                    is GlobalState.Loading -> Log.d("POPULARLISTVIEWMODEL", "LOADING")
                }
            }
        }
    }

    private suspend fun parseMovies(popularList: List<PopularMovie>, favList: List<PopularMovie>) = withContext(Dispatchers.Default){
        if (!popularList.isEmpty()){
            popularMovies = popularList
        }

        if (!favList.isEmpty()){
            favMovies = favList
        }

        var popList: MutableList<PopularMovie> = mutableListOf()
        if (!(popularMovies.isEmpty() && favMovies.isEmpty())){
            for(movie in popularMovies){
                var data = PopularMovie(movie.id, movie.title, movie.popularity, movie.vote, movie.backdrop_path, favMovies.contains(movie))
                popList.add(data)
            }
            if (!popList.isEmpty())
                _setListState.postValue(SetState.SuccessWithMatchMovies(popList))
        }
    }

    private fun loadMovies() {
        val db = Room.databaseBuilder(
            getApplication<Application>().applicationContext,
            AppDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()

        viewModelScope.launch(exceptionHandler) {
            var popularResponse = withContext(Dispatchers.IO) {
                mdbApi.getMovies()
            }
            var popList: MutableList<PopularMovie> = mutableListOf()
            for(movie in popularResponse.results){
                var data = PopularMovie(movie.id, movie.title, movie.popularity, movie.vote, movie.backdrop_path, false)
                popList.add(data)
            }
            movieRepository.setPop(popList)
        }
        viewModelScope.launch {
            var popularResponse = withContext(Dispatchers.Default){
                db.movieDao().getAll()
            }
            movieRepository.setFav(popularResponse)
        }
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun onFavClicked(movie: PopularMovie) {
        movieRepository.addFromFav(movie, getDB())
    }

    fun onDelClicked(movie: PopularMovie) {
        movieRepository.removeFromFav(movie, getDB())
    }
    private fun getDB(): AppDatabase {
        return Room.databaseBuilder(
            getApplication<Application>().applicationContext,
            AppDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()
    }
}

sealed class SetState {
    data class SuccessWithMatchMovies(val popularMovies: List<PopularMovie>):SetState()
    data class Success(val popularMovies: List<PopularMovie>, val favMovies: List<PopularMovie> ) : SetState()
    data class Error(val message: String) : SetState()
    data class Loading(val message: String) : SetState()
}