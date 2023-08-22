package com.example.weatherapp.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchedData::class], version = 1)

abstract class SearchedDatabase: RoomDatabase() {
    abstract fun SearchDao():SearchDao
}