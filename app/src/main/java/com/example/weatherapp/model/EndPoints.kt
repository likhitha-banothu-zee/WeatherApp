package com.example.weatherapp.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface EndPoints {

    @GET("data/2.5/weather?")
    suspend fun getWeather(
        @Query("q") city:String,
        @Query("units") units:String,
        @Query("appid") apiKey:String,
    ): Response<WeatherData>

    @GET
    suspend fun getCurrentWeather(@Url url:String) : Response<WeatherData>


    @GET
    suspend fun getCity(@Url url:String) : Response<Cities>
}