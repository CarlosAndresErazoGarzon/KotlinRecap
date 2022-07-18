package com.example.taller2.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taller2.model.User
import com.example.taller2.model.Movie
import com.example.taller2.movie_list.popular.PopularMovie

@Database(entities = [User::class, PopularMovie::class], version = 5)
abstract class AppDatabase:RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao
}