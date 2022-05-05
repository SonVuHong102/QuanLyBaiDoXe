package com.zagon102.quanlybaidoxe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VehicleListAdapter(private val vehicleCheckList: List<VehicleCheck>,private val onItemClick: ((VehicleCheck) -> Unit)?): RecyclerView.Adapter<VehicleListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brand = itemView.findViewById<TextView>(R.id.text_brand)
        val seats = itemView.findViewById<TextView>(R.id.text_seats)
        val color = itemView.findViewById<TextView>(R.id.text_color)
        val plate = itemView.findViewById<TextView>(R.id.text_plate)
        val checkin = itemView.findViewById<TextView>(R.id.text_checkin_date)
        val name = itemView.findViewById<TextView>(R.id.text_name)
        val phone = itemView.findViewById<TextView>(R.id.text_phone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_item_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = vehicleCheckList[position]
        holder.brand.text = item.brand
        holder.seats.text = item.seats.toString()
        holder.color.text = item.color
        holder.plate.text = item.plate
        holder.checkin.text = item.checkInDate.toDateFormat()
        holder.name.text = item.name
        holder.phone.text = item.phone
        holder.itemView.setOnClickListener{
            onItemClick?.let { it1 -> it1(item) }
        }
    }

    override fun getItemCount(): Int {
       return vehicleCheckList.size
    }
}