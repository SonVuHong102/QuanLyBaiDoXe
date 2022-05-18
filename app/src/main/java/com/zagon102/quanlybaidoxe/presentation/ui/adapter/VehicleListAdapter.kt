package com.zagon102.quanlybaidoxe.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zagon102.quanlybaidoxe.R
import com.zagon102.quanlybaidoxe.presentation.model.Vehicle

class VehicleListAdapter(private val vehicleList: List<Vehicle>): RecyclerView.Adapter<VehicleCheckListAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val brand = itemView.findViewById<TextView>(R.id.text_brand)
        val seats = itemView.findViewById<TextView>(R.id.text_seats)
        val color = itemView.findViewById<TextView>(R.id.text_color)
        val plate = itemView.findViewById<TextView>(R.id.text_plate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehicleCheckListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_item_row,parent,false)
        return VehicleCheckListAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleCheckListAdapter.ViewHolder, position: Int) {
        val item = vehicleList[position]
        holder.brand.text = item.brand
        holder.seats.text = item.seats.toString()
        holder.color.text = item.color
        holder.plate.text = item.plate
    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }


}