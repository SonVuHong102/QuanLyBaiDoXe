package com.zagon102.quanlybaidoxe.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.zagon102.quanlybaidoxe.R
import com.zagon102.quanlybaidoxe.data.DBHelper
import com.zagon102.quanlybaidoxe.presentation.model.User
import com.zagon102.quanlybaidoxe.presentation.model.Vehicle
import com.zagon102.quanlybaidoxe.presentation.model.VehicleCheck
import com.zagon102.quanlybaidoxe.presentation.module.UserInfoModule
import com.zagon102.quanlybaidoxe.presentation.ui.adapter.VehicleCheckListAdapter
import com.zagon102.quanlybaidoxe.presentation.ui.adapter.VehicleListAdapter
import com.zagon102.quanlybaidoxe.ultis.toLocalDate

class VehicleManagementActivity : AppCompatActivity() {
    private lateinit var vehicleListView: RecyclerView
    private lateinit var backButton: Button
    private lateinit var createButton: Button
    private lateinit var db: DBHelper

    private lateinit var adapter: VehicleListAdapter
    private lateinit var vehicleList: MutableList<Vehicle>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_management)
        db = DBHelper(this,null)
        initViews()
    }

    private fun initViews() {
        vehicleListView = findViewById(R.id.vehicle_list)
        vehicleList = mutableListOf()
        adapter = VehicleListAdapter(vehicleList)
        vehicleListView.adapter = adapter

        createButton = findViewById(R.id.button_create_account)
        createButton.setOnClickListener{
            startActivity(Intent(this,CreateNewVehicleActivity::class.java))
        }
        backButton = findViewById(R.id.button_back)
        backButton.setOnClickListener{
            finish()
        }
    }

    private fun getData() {
        vehicleList.clear()
        UserInfoModule.user?.let {
            val cursor = db?.getVehicles(it.username,it.phone)
            if(cursor != null && cursor.moveToFirst()) {
                do {
                    vehicleList.add(
                        Vehicle(
                            cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.ID_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERNAME_COl)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.BRAND_COL)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.SEATS_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLOR_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PLATE_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PHONE_COL)),
                        )
                    )
                } while (cursor.moveToNext())
            } else {
                Toast.makeText(this,"Empty", Toast.LENGTH_SHORT).show()
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        getData()
        super.onResume()
    }

    override fun finish() {
        db.close()
        super.finish()
    }
}