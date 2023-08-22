package com.example.weatherapp.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val baseUrl1="https://api.openweathermap.org/"
    val getInstance : EndPoints by lazy {
        Retrofit.Builder().baseUrl(baseUrl1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EndPoints::class.java)
    }

    private val baseUrl2 = "https://autocomplete.search.hereapi.com/v1/"
    val apiCities: EndPoints by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EndPoints::class.java)
    }

}
