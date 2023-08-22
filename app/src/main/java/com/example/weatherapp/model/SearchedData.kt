package com.example.weatherapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Locale

@Entity(indices = [Index(value = ["Name"], unique = true)])
data class SearchedData(
        @PrimaryKey(autoGenerate = true)val uid: Int,
        @ColumnInfo(name="Name") var name: String
    ) {
}