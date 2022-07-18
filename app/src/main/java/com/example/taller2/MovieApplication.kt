package com.example.taller2

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import androidx.room.Room
import com.example.taller2.db.AppDatabase
import com.example.taller2.model.User
import com.example.taller2.movie_list.repository.MovieRepository
import kotlinx.coroutines.*

class MovieApplication: Application(){


    val moviesRepository = MovieRepository(MainScope())

    override fun onCreate() {
        super.onCreate()

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()

        val dato = User("user", "pass")
        val dato1 = User("user1", "pass1")
        val dato2 = User("user2", "pass2")

        MainScope().launch {
            withContext(Dispatchers.Default){
                db.userDao().insertUsers(dato)
                db.userDao().insertUsers(dato2)
                db.userDao().insertUsers(dato1)
                Log.d("DATOSDB", db.userDao().getAll().toString())
                Log.d("DATOSDB", db.movieDao().getAll().toString())
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}
