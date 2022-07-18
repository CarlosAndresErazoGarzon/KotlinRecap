package com.example.taller2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val user: String,
    @ColumnInfo(name= "pass")val pass: String
    ) {
}