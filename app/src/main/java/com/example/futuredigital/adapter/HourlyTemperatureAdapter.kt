package com.example.futuredigital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.futuredigital.BR
import com.example.futuredigital.databinding.HourlyTemperatureAdapterBinding
import com.example.moduleapiservices.models.CustomizedClassToDisplayInAdaper

class HourlyTemperatureAdapter(
    val context: Context,
    private val itemList: List<CustomizedClassToDisplayInAdaper>
) : RecyclerView.Adapter<HourlyTemperatureAdapter.Myhandler>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myhandler {
        val hourlyTemperatureAdapterBinding =
            HourlyTemperatureAdapterBinding.inflate(LayoutInflater.from(context), parent, false)
        return Myhandler(
            hourlyTemperatureAdapterBinding
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Myhandler, position: Int) {
        holder.bind(itemList.get(position))
    }

    class Myhandler(val binding: HourlyTemperatureAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(customizedClassToDisplayInAdaper: CustomizedClassToDisplayInAdaper) {
            binding.hourlyforecastAdapter = customizedClassToDisplayInAdaper
            binding.setVariable(BR.hourlyforecastAdapter, customizedClassToDisplayInAdaper)
            binding.executePendingBindings()
        }
    }
}