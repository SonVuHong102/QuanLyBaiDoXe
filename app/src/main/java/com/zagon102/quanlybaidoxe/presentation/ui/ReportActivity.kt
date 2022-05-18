package com.zagon102.quanlybaidoxe.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zagon102.quanlybaidoxe.R
import com.zagon102.quanlybaidoxe.data.DBHelper
import com.zagon102.quanlybaidoxe.presentation.model.VehicleCheck
import com.zagon102.quanlybaidoxe.presentation.module.UserInfoModule
import com.zagon102.quanlybaidoxe.presentation.ui.adapter.VehicleCheckListAdapter
import com.zagon102.quanlybaidoxe.ultis.Constants
import com.zagon102.quanlybaidoxe.ultis.toLocalDate

class ReportActivity : AppCompatActivity() {
    private lateinit var vehicleCheckListView: RecyclerView
    private lateinit var backButton: Button
    private lateinit var adapter: VehicleCheckListAdapter
    private lateinit var vehicleCheckList: MutableList<VehicleCheck>
    private lateinit var currentVehicleCheckList: MutableList<VehicleCheck>
    private lateinit var searchText: TextInputEditText
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
        if(UserInfoModule.user!!.role == Constants.CUSTOMER) {
            findViewById<TextInputLayout>(R.id.text_username_container).visibility = View.GONE
        }
        searchText = findViewById(R.id.text_search)
        searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                currentVehicleCheckList.clear()
                if(s.isNotEmpty()) {
                    for (i in vehicleCheckList) {
                        if (i.phone.contains(s)) {
                            currentVehicleCheckList.add(i)
                        }
                    }
                } else {
                    currentVehicleCheckList.addAll(vehicleCheckList)
                }
                adapter.notifyDataSetChanged()
            }
        })
        backButton = findViewById(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }
        vehicleCheckListView = findViewById(R.id.vehicle_check_list)
        vehicleCheckList = mutableListOf()
        currentVehicleCheckList = mutableListOf()
        adapter = VehicleCheckListAdapter(currentVehicleCheckList,true,null)
        vehicleCheckListView.adapter = adapter
    }

    private fun getData() {
        vehicleCheckList.clear()
        UserInfoModule.user?.let {
            val cursor = if(it.role == Constants.CUSTOMER) db?.getChecks(it.phone) else db?.getChecks()
            if(cursor != null && cursor.moveToFirst()) {
                do {
                    vehicleCheckList.add(
                        VehicleCheck(
                            cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.ID_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.BRAND_COL)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.SEATS_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLOR_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PLATE_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.LOCATION_COL)),
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
        }
        currentVehicleCheckList.addAll(vehicleCheckList)
        adapter.notifyDataSetChanged()
    }
}