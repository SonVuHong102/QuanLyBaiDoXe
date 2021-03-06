package com.zagon102.quanlybaidoxe.presentation.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.zagon102.quanlybaidoxe.*
import com.zagon102.quanlybaidoxe.data.DBHelper
import com.zagon102.quanlybaidoxe.presentation.model.VehicleCheck
import com.zagon102.quanlybaidoxe.ultis.Constants
import com.zagon102.quanlybaidoxe.ultis.hideButton
import com.zagon102.quanlybaidoxe.ultis.toDateFormat
import com.zagon102.quanlybaidoxe.ultis.toLocalDate
import java.time.LocalDate
import java.util.*


class VehicleInCustomActivity : AppCompatActivity() {
    private lateinit var locationSpinner: Spinner
    private lateinit var brandAutoComplete: AutoCompleteTextView
    private lateinit var seatsAutoComplete: AutoCompleteTextView
    private lateinit var colorsAutoComplete: AutoCompleteTextView
    private lateinit var plateText: TextView
    private lateinit var checkinDateText: TextView
    private lateinit var buttonDatepicker: ImageButton
    private lateinit var nameText: TextView
    private lateinit var phoneText: TextView
    private lateinit var buttonBack: Button
    private lateinit var buttonConfirm: Button
    private lateinit var db: DBHelper
    private var vehicleCheck: VehicleCheck? = null
    private var parkingLocation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_in_custom)
        db = DBHelper(this,null)
        initViews()
    }

    private fun initViews() {
        hideButton()
        locationSpinner = findViewById(R.id.location_spinner)
        brandAutoComplete = findViewById(R.id.auto_complete_brand)
        seatsAutoComplete = findViewById(R.id.auto_complete_seat)
        colorsAutoComplete = findViewById(R.id.auto_complete_color)
        plateText = findViewById(R.id.text_plate)
        checkinDateText = findViewById(R.id.text_checkin_date)
        buttonDatepicker = findViewById(R.id.button_datepicker)
        nameText = findViewById(R.id.text_name)
        phoneText = findViewById(R.id.text_phone)
        buttonBack = findViewById(R.id.button_back)
        buttonConfirm = findViewById(R.id.button_confirm)

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

        brandAutoComplete.setAdapter(
            ArrayAdapter.createFromResource(
                this,
                R.array.brand_array,
                android.R.layout.simple_list_item_1
            )
        )
        brandAutoComplete.threshold = 1


        seatsAutoComplete.setAdapter(
            ArrayAdapter.createFromResource(
                this,
                R.array.seat_array,
                android.R.layout.simple_list_item_1
            )
        )
        seatsAutoComplete.threshold = 1

        colorsAutoComplete.setAdapter(
            ArrayAdapter.createFromResource(
                this,
                R.array.color_array,
                android.R.layout.simple_list_item_1
            )
        )
        colorsAutoComplete.threshold = 1

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

        buttonConfirm.setOnClickListener {
            vehicleCheck = VehicleCheck(
                null,
                brandAutoComplete.text.toString(),
                seatsAutoComplete.text.toString().toInt(),
                colorsAutoComplete.text.toString(),
                plateText.text.toString(),
                parkingLocation!!,
                checkinDateText.text.toString().toLocalDate(),
                null,
                nameText.text.toString(),
                phoneText.text.toString(),
                null,
                Constants.PENDING
            )
            showDialog(vehicleCheck!!)
        }
        buttonBack.setOnClickListener{
            finish()
        }
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
        dialog.findViewById<TextView>(R.id.text_location).text = vehicleCheck.location
        dialog.findViewById<TextView>(R.id.text_checkin_date).text =
            vehicleCheck.checkInDate.toDateFormat()
        dialog.findViewById<LinearLayout>(R.id.checkout_layout).visibility = View.GONE
        dialog.findViewById<TextView>(R.id.text_name).text = vehicleCheck.name
        dialog.findViewById<TextView>(R.id.text_phone).text = vehicleCheck.phone
        dialog.findViewById<LinearLayout>(R.id.cash_layout).visibility = View.GONE
        val yesBtn = dialog.findViewById<Button>(R.id.button_checkout)
        yesBtn.text = getString(R.string.checkin)
        val noBtn = dialog.findViewById<Button>(R.id.button_cancel)

        yesBtn.setOnClickListener {
            dialog.dismiss()
            db.addCheck(vehicleCheck)
            Toast.makeText(this,"Vehicle Checkin Completed",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MenuActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Removes other Activities from stack
            startActivity(intent)
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun finish() {
        db.close()
        super.finish()
    }
}