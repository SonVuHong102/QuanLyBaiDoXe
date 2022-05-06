package com.zagon102.quanlybaidoxe.presentation.ui

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.RecyclerView
import com.zagon102.quanlybaidoxe.R
import com.zagon102.quanlybaidoxe.data.DBHelper
import com.zagon102.quanlybaidoxe.presentation.model.VehicleCheck
import com.zagon102.quanlybaidoxe.presentation.ui.adapter.VehicleListAdapter
import com.zagon102.quanlybaidoxe.ultis.Constants
import com.zagon102.quanlybaidoxe.ultis.toCurrency
import com.zagon102.quanlybaidoxe.ultis.toDateFormat
import com.zagon102.quanlybaidoxe.ultis.toLocalDate
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ReportActivity : AppCompatActivity() {
    private lateinit var vehicleCheckListView: RecyclerView
    private lateinit var backButton: Button
    private lateinit var adapter: VehicleListAdapter
    private lateinit var vehicleCheckList: MutableList<VehicleCheck>
    private var db: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        db = DBHelper(this,null)
        initViews()
    }

    override fun onResume() {
        getData()
        super.onResume()
    }

    private fun initViews() {
        backButton = findViewById(R.id.button_back)
        backButton.setOnClickListener{
            finish()
        }
        vehicleCheckListView = findViewById(R.id.vehicle_check_list)
        vehicleCheckList = mutableListOf()
        adapter = VehicleListAdapter(vehicleCheckList,true,null)
        vehicleCheckListView.adapter = adapter
    }

    private fun getData() {
        vehicleCheckList.clear()
        val cursor = db?.getChecks()
        if(cursor != null && cursor.moveToFirst()) {
            do {
                vehicleCheckList.add(
                    VehicleCheck(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.ID_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.BRAND_COL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.SEATS_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLOR_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PLATE_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CHECKIN_COL)).toLocalDate(),
                        cursor.getStringOrNull(cursor.getColumnIndexOrThrow(DBHelper.CHECKOUT_COL))?.toLocalDate(),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NAME_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PHONE_COL)),
                        cursor.getLongOrNull(cursor.getColumnIndexOrThrow(DBHelper.CASH_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DONE_COL))
                    ))
            } while (cursor.moveToNext())
        } else {
            Toast.makeText(this,"Empty", Toast.LENGTH_SHORT).show()
        }
        adapter.notifyDataSetChanged()
    }
}