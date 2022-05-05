package com.zagon102.quanlybaidoxe

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate


class VehicleOutActivity : AppCompatActivity() {
    private lateinit var vehicleCheckList: RecyclerView
    private lateinit var backButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_out)

        initViews()
    }

    private fun initViews() {
        backButton = findViewById(R.id.button_back)
        backButton.setOnClickListener{
            finish()
        }
        vehicleCheckList = findViewById(R.id.vehicle_check_list)
        val list = VehicleCheck.getVehicleChecks()
        val adapter = VehicleListAdapter(list,onItemClick)
        vehicleCheckList.adapter = adapter
    }

    private val onItemClick: (VehicleCheck) -> Unit = {
        showDialog(it)
    }

    private fun showDialog(vehicleCheck: VehicleCheck) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_checkout)
        dialog.findViewById<TextView>(R.id.text_brand).text = vehicleCheck.brand
        dialog.findViewById<TextView>(R.id.text_seats).text = vehicleCheck.seats.toString()
        dialog.findViewById<TextView>(R.id.text_color).text = vehicleCheck.color
        dialog.findViewById<TextView>(R.id.text_plate).text = vehicleCheck.plate
        dialog.findViewById<TextView>(R.id.text_checkin_date).text = vehicleCheck.checkInDate.toDateFormat()
        dialog.findViewById<TextView>(R.id.text_name).text = vehicleCheck.name
        dialog.findViewById<TextView>(R.id.text_phone).text = vehicleCheck.phone
        val yesBtn = dialog.findViewById<Button>(R.id.button_checkout)
        val noBtn = dialog.findViewById<Button>(R.id.button_cancel)

        yesBtn.setOnClickListener {
            dialog.dismiss()
            vehicleCheck.checkOutDate = LocalDate.now()
            val bundle =  Bundle()
            bundle.putSerializable(Constants.VEHICLE_CHECK,vehicleCheck)
            val intent = Intent(this,CheckOutCompleteActivity::class.java)
            intent.putExtra(Constants.BUNDLE,bundle)
            startActivity(intent)
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}