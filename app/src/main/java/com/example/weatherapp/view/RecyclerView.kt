package com.example.weatherapp.view

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.location
import com.example.weatherapp.viewModel.CustomViewModel

class MyAdapter(
    private val binding: ActivityMainBinding,
    private val viewModel: CustomViewModel,
    private val context: Context,
    private val data: ArrayList<location>
): RecyclerView.Adapter<MyAdapter.ViewHolder> (){

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val searchList=view.findViewById<TextView>(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.search_list,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.searchList.text=data[position].city
        holder.searchList.setOnClickListener {
            viewModel.getWeather(data[position].city!!)
            binding.recyclerView.visibility = View.INVISIBLE
        }
    }

}

