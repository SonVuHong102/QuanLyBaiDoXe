package com.zagon102.quanlybaidoxe.presentation.ui

import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.zagon102.quanlybaidoxe.R
import com.zagon102.quanlybaidoxe.data.DBHelper
import com.zagon102.quanlybaidoxe.presentation.model.Vehicle
import com.zagon102.quanlybaidoxe.presentation.model.VehicleCheck
import com.zagon102.quanlybaidoxe.presentation.module.UserInfoModule
import com.zagon102.quanlybaidoxe.ultis.Constants
import com.zagon102.quanlybaidoxe.ultis.hideButton
import com.zagon102.quanlybaidoxe.ultis.toDateFormat
import com.zagon102.quanlybaidoxe.ultis.toLocalDate
import java.time.LocalDate
import java.util.*

class CreateNewVehicleActivity : AppCompatActivity() {
    private lateinit var brandAutoComplete: AutoCompleteTextView
    private lateinit var seatsAutoComplete: AutoCompleteTextView
    private lateinit var colorsAutoComplete: AutoCompleteTextView
    private lateinit var plateText: TextView
    private lateinit var buttonConfirm: Button
    private lateinit var buttonBack: Button
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_vehicle)
        db = DBHelper(this,null)
        initViews()
    }

    private fun initViews() {
        hideButton()
        brandAutoComplete = findViewById(R.id.auto_complete_brand)
        seatsAutoComplete = findViewById(R.id.auto_complete_seat)
        colorsAutoComplete = findViewById(R.id.auto_complete_color)
        plateText = findViewById(R.id.text_plate)
        buttonConfirm = findViewById(R.id.button_confirm)
        buttonBack = findViewById(R.id.button_back)

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

        buttonConfirm.setOnClickListener {
            if(brandAutoComplete.text.toString().isNullOrEmpty() ||  seatsAutoComplete.text.toString().isNullOrEmpty() || colorsAutoComplete.text.toString().isNullOrEmpty() || plateText.text.toString().isNullOrEmpty()) {
                Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val vehicle = Vehicle(
                null,
                UserInfoModule.user!!.username,
                brandAutoComplete.text.toString(),
                seatsAutoComplete.text.toString().toInt(),
                colorsAutoComplete.text.toString(),
                plateText.text.toString(),
                UserInfoModule.user!!.phone
            )
            db.addVehicle(vehicle)
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            finish()
        }

        buttonBack.setOnClickListener{
            finish()
        }
    }

    override fun finish() {
        db.close()
        super.finish()
    }
}