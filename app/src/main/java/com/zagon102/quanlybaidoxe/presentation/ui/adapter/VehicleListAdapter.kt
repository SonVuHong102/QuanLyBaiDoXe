package com.zagon102.quanlybaidoxe.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zagon102.quanlybaidoxe.R
import com.zagon102.quanlybaidoxe.presentation.model.VehicleCheck
import com.zagon102.quanlybaidoxe.ultis.toCurrency
import com.zagon102.quanlybaidoxe.ultis.toDateFormat

class VehicleListAdapter(private val vehicleCheckList: List<VehicleCheck>, private val isReport: Boolean = false, private val onItemClick: ((VehicleCheck) -> Unit)?): RecyclerView.Adapter<VehicleListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brand = itemView.findViewById<TextView>(R.id.text_brand)
        val seats = itemView.findViewById<TextView>(R.id.text_seats)
        val color = itemView.findViewById<TextView>(R.id.text_color)
        val plate = itemView.findViewById<TextView>(R.id.text_plate)
        val checkin = itemView.findViewById<TextView>(R.id.text_checkin_date)
        val checkout = itemView.findViewById<TextView>(R.id.text_checkout_date)
        val name = itemView.findViewById<TextView>(R.id.text_name)
        val phone = itemView.findViewById<TextView>(R.id.text_phone)
        val cash = itemView.findViewById<TextView>(R.id.text_cash)

        fun checkIsReport(isReport: Boolean) {
            if(isReport) {
                itemView.findViewById<LinearLayout>(R.id.checkout_layout).visibility = View.VISIBLE
                itemView.findViewById<LinearLayout>(R.id.cash_layout).visibility = View.VISIBLE
            } else {
                itemView.findViewById<LinearLayout>(R.id.checkout_layout).visibility = View.GONE
                itemView.findViewById<LinearLayout>(R.id.cash_layout).visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_item_row,parent,false)
        val viewHolder = ViewHolder(view)
        viewHolder.checkIsReport(isReport)
        return viewHolder
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
        if(isReport) {
            holder.checkout.text = item.checkOutDate?.toDateFormat() ?: ""
            if(item.cash == null) {
                holder.cash.text = ""
            } else {
                holder.cash.text = item.cash!!.toCurrency()
            }
        }
        holder.itemView.setOnClickListener{
            onItemClick?.let { it1 -> it1(item) }
        }
    }

    override fun getItemCount(): Int {
       return vehicleCheckList.size
    }
}