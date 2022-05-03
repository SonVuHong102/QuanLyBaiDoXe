package com.zagon102.quanlybaidoxe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView


class VehicleOutActivity : AppCompatActivity() {
    private lateinit var vehicleCheckList: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_out)

        initViews()
    }

    private fun initViews() {
        vehicleCheckList = findViewById(R.id.vehicle_check_list)
        val list = VehicleCheck.getVehicleChecks()
        val adapter = VehicleListAdapter(list)
        vehicleCheckList.adapter = adapter
    }
}