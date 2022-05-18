package com.zagon102.quanlybaidoxe.presentation.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.zagon102.quanlybaidoxe.R
import com.zagon102.quanlybaidoxe.data.DBHelper
import com.zagon102.quanlybaidoxe.presentation.model.Vehicle
import com.zagon102.quanlybaidoxe.presentation.model.VehicleCheck
import com.zagon102.quanlybaidoxe.ultis.Constants
import com.zagon102.quanlybaidoxe.ultis.toDateFormat
import com.zagon102.quanlybaidoxe.ultis.toLocalDate
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*


class VehicleInActivity : AppCompatActivity() {
    private lateinit var locationSpinner: Spinner
    private lateinit var phoneText: EditText
    private lateinit var phoneButton: ImageButton
    private lateinit var vehicleSpinner: Spinner
    private lateinit var checkinDateText: TextView
    private lateinit var buttonDatepicker: ImageButton
    private lateinit var newCustomerButton: Button
    private lateinit var backButton: Button
    private lateinit var confirmButton: Button
    private lateinit var db: DBHelper
    private lateinit var vehicleAdapter: ArrayAdapter<Vehicle>
    private lateinit var vehicleList: MutableList<Vehicle>
    private var parkingLocation: String? = null
    private var vehicle: Vehicle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_in)
        db = DBHelper(this,null)
        initViews()
    }

    private fun initViews() {
        locationSpinner = findViewById(R.id.location_spinner)
        phoneText = findViewById(R.id.text_phone)
        phoneButton = findViewById(R.id.button_phone)
        vehicleSpinner = findViewById(R.id.vehicle_spinner)
        checkinDateText = findViewById(R.id.text_checkin_date)
        buttonDatepicker = findViewById(R.id.button_datepicker)
        newCustomerButton = findViewById(R.id.button_newcustomer)
        backButton = findViewById(R.id.button_back)
        confirmButton = findViewById(R.id.button_confirm)

        val locationAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.parkingLocations
        )
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationSpinner.adapter = locationAdapter
        locationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parkingLocation =  Constants.parkingLocations[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        phoneText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                vehicleList.clear()
                vehicleAdapter.notifyDataSetChanged()
            }
        })

        phoneButton.setOnClickListener{
            if (phoneText.text.isNullOrEmpty()) {
                Toast.makeText(this, "Empty phone number", Toast.LENGTH_SHORT).show()
            }
            getVehicleData()
        }

        vehicleList = mutableListOf()
        vehicleAdapter = ArrayAdapter<Vehicle>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            vehicleList
        )
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vehicleSpinner.adapter = vehicleAdapter

        vehicleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                vehicle =  vehicleList[position]
                Log.e("Checking","on selected : ${vehicle == null}")

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vehicle = null
                Log.e("Checking","on nothing : ${vehicle == null}")
            }
        }


        buttonDatepicker.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                val date = LocalDate.of(year,monthOfYear+1,dayOfMonth)
                checkinDateText.text = date.toDateFormat()
            }, year, month, day)
            dpd.datePicker.maxDate = Date().time
            dpd.show()
            dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        }

        newCustomerButton.setOnClickListener{
            startActivity(Intent(this,VehicleInCustomActivity::class.java))
        }

        confirmButton.setOnClickListener{
            vehicle?.let {
                val cursor = db?.getUserByPhone(phoneText.text.toString())
                if(cursor != null && cursor.moveToFirst()) {
                    val vehicleCheck = VehicleCheck(
                        null,
                        it.brand,
                        it.seats,
                        it.color,
                        it.plate,
                        parkingLocation!!,
                        checkinDateText.text.toString().toLocalDate(),
                        null,
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NAME_COL)),
                        phoneText.text.toString(),
                        null,
                        Constants.PENDING
                    )
                    db?.addCheck(vehicleCheck)
                    Toast.makeText(this,"Vehicle Checkin Completed",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            Toast.makeText(this,"No vehicle selected",Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        backButton.setOnClickListener{
            finish()
        }
    }

    private fun getVehicleData() {
        vehicleList.clear()
        if (phoneText.text.isNotEmpty()) {
            val cursor = db?.getVehicles("", phoneText.text.toString())
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    vehicleList.add(
                        Vehicle(
                            cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.ID_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERNAME_COl)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.BRAND_COL)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.SEATS_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLOR_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PLATE_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PHONE_COL))
                        )
                    )
                } while (cursor.moveToNext())
            } else {
                Toast.makeText(this, "Empty List", Toast.LENGTH_SHORT).show()
            }
        }
        vehicleAdapter.notifyDataSetChanged()
    }

    override fun finish() {
        db.close()
        super.finish()
    }
}