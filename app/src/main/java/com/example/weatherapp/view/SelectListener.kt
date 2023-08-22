package com.example.weatherapp.view

import com.example.weatherapp.model.SearchedData
import java.text.FieldPosition

interface SelectListener {
    fun onItemClick(allSearch:List<SearchedData>,position: Int)
}