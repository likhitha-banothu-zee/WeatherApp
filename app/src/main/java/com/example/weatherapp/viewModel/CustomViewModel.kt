package com.example.weatherapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.RetrofitHelper
import com.example.weatherapp.model.WeatherData
import com.example.weatherapp.model.location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CustomViewModel(): ViewModel() {
    private var result = MutableLiveData<WeatherData>()
    private var result2:ArrayList<location> = ArrayList()
    val data2 = MutableLiveData<ArrayList<location>>()

    val data : LiveData<WeatherData>
        get() {
            return result
        }

    fun getWeather(city:String) {
        GlobalScope.launch (Dispatchers.IO){
            val apiData = RetrofitHelper.getInstance.getWeather(city,"metric","1fdb0737d2cb43f7d78d9b1ac8f7f53d")
            apiData.body()?.let {
                result.postValue(it)
            }
        }
    }

    fun getCurrentData(lat:String,lon:String){
        val key = "88c0154a25fb21fb0d30003e6956fb4c"
        var endPoint = "data/2.5/weather?lat=$lat&lon=$lon&appid=$key"
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val apiData = RetrofitHelper.getInstance.getCurrentWeather(endPoint)
                apiData?.body()?.let {
                    result.postValue(it)
                }
                Log.d("check",apiData.toString())
            } catch (e: Exception) {
                Log.d("error",e.toString())
            }
        }
    }


    fun getCities(pattern:String){
        val key = "fKzVotCk8CmC2teZ3QEn0f9XoL_sZAV585wgTMKFoao"
        var endPoint = "autocomplete?q=$pattern&apiKey=$key"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val apiData = RetrofitHelper.apiCities.getCity(endPoint)
                apiData?.body()?.let {
                    val items = apiData.body()?.items
                    items?.let {
                        val labels:ArrayList<location> = ArrayList()
                        for (i in items){
                            labels.add(location(i.address.city))
                        }
                        result2 = labels
                        Log.d("API Response",result2.toString())
                        data2.postValue(result2)
                    }
                }
            } catch (e: Exception) {
               Log.d("error",e.toString())
            }
        }
    }
}