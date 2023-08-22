package com.example.weatherapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface SearchDao {
        @Insert
        suspend fun insertCity(allSearch: SearchedData)

        @Query("SELECT * FROM SearchedData")
        suspend fun getData():List<SearchedData>

        @Query("SELECT * FROM SearchedData WHERE Name LIKE :searchQuery ")
        fun searchDatabase(searchQuery: String): Flow<List<SearchedData>>

}