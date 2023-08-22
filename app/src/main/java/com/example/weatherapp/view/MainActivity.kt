package com.example.weatherapp.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.location
import com.example.weatherapp.viewModel.CustomViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: CustomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        viewModel = ViewModelProvider(this).get(CustomViewModel::class.java)
        checkLocationPermissions()
        getCurrentLocation()

        binding.searchedcity.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.getWeather(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                newText?.let {
                    if(it.length>=3){
                        binding.recyclerView.visibility = View.VISIBLE
                        viewModel.getCities(it)
                    }
                }
                return true
            }

        })

        viewModel.data.observe(this, Observer {
            binding.location.text=it.name
            binding.weatherCondition.text="${it.weather[0].main}: ${it.weather[0].description}"
            binding.degrees.text="${it.main.temp}ºC"
            binding.maxTemp.text="${it.main.temp_max}ºC"
            binding.minTemp.text="${it.main.temp_min}ºC"
            val iconId=it.weather[0].icon
            val imgUrl="https://openweathermap.org/img/w/${iconId}.png"
            Picasso.get().load(imgUrl).into(binding.imageView)
        })

        viewModel.data2.observe(this, Observer {
            prepareRecyclerView(binding,viewModel,it)
        })
    }

    private fun getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("check","Not yet Started")
            return
        }
        Log.d("check","Started")
        fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task ->
            val location = task.result
            if(location==null){
                Toast.makeText(this,"Null Received", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"Get Success", Toast.LENGTH_SHORT).show()
                viewModel.getCurrentData(location.latitude.toString(),location.longitude.toString())
            }
        }
    }

    fun prepareRecyclerView(binding:ActivityMainBinding,viewModel: CustomViewModel, data: ArrayList<location>){
        this.binding.recycler.layoutManager = LinearLayoutManager(this)
        val adapter = MyAdapter(binding,viewModel,this,data)
        this.binding.recycler.adapter = adapter
    }

    private fun checkLocationPermissions(){
        if(!isLocationEnabled()){
            Toast.makeText(this,"Turn on Location", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        if(!checkPermissions()) requestPermission()
    }

    private fun isLocationEnabled():Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isLocationEnabled
    }


    private fun checkPermissions():Boolean{
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 200
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            }
            else{
                Toast.makeText(this,"Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}