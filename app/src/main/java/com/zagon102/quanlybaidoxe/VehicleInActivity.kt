package com.zagon102.quanlybaidoxe

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class VehicleInActivity : AppCompatActivity() {
    private lateinit var brandAutoComplete: AutoCompleteTextView
    private lateinit var seatsAutoComplete: AutoCompleteTextView
    private lateinit var colorsAutoComplete: AutoCompleteTextView
    private lateinit var plateText: TextView
    private lateinit var checkinDateText: TextView
    private lateinit var buttonDatepicker: ImageButton
    private lateinit var nameText: TextView
    private lateinit var phoneText: TextView
    private lateinit var buttonCancel: Button
    private lateinit var buttonConfirm: Button
    private lateinit var db: DBHelper
    var vehicleCheck: VehicleCheck? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_in)
        db = DBHelper(this,null)
        initViews()
    }

    private fun initViews() {
        hideButton()
        brandAutoComplete = findViewById(R.id.auto_complete_brand)
        seatsAutoComplete = findViewById(R.id.auto_complete_seat)
        colorsAutoComplete = findViewById(R.id.auto_complete_color)
        plateText = findViewById(R.id.text_plate)
        checkinDateText = findViewById(R.id.text_checkin_date)
        buttonDatepicker = findViewById(R.id.button_datepicker)
        nameText = findViewById(R.id.text_name)
        phoneText = findViewById(R.id.text_phone)
        buttonCancel = findViewById(R.id.button_cancel)
        buttonConfirm = findViewById(R.id.button_confirm)

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
                checkinDateText.text = "$dayOfMonth/${monthOfYear+1}/$year"
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
                checkinDateText.text.toString().toLocalDate(),
                null,
                nameText.text.toString(),
                phoneText.text.toString(),
                null,
                Constants.PENDING
            )

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
        dialog.findViewById<TextView>(R.id.text_checkin_date).text =
            vehicleCheck.checkInDate.toDateFormat()
        dialog.findViewById<TextView>(R.id.text_name).text = vehicleCheck.name
        dialog.findViewById<TextView>(R.id.text_phone).text = vehicleCheck.phone
        val yesBtn = dialog.findViewById<Button>(R.id.button_checkout)
        yesBtn.text = getString(R.string.checkin)
        val noBtn = dialog.findViewById<Button>(R.id.button_cancel)

        yesBtn.setOnClickListener {
            dialog.dismiss()
            db.addCheck(vehicleCheck)
            Toast.makeText(this,"Vehicle Checkin Completed",Toast.LENGTH_SHORT).show()
            finish()
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