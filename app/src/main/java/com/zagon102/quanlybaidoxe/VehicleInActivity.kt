package com.zagon102.quanlybaidoxe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class VehicleInActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var brandAutoComplete: AutoCompleteTextView
    private lateinit var seatsAutoComplete: AutoCompleteTextView
    private lateinit var colorsAutoComplete: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_in)

        initViews()
    }

    private fun initViews() {
        hideButton()
        brandAutoComplete = findViewById(R.id.auto_complete_brand)
        seatsAutoComplete = findViewById(R.id.auto_complete_seat)
        colorsAutoComplete = findViewById(R.id.auto_complete_color)

        brandAutoComplete.setAdapter(ArrayAdapter.createFromResource(
            this,
            R.array.brand_array,
            android.R.layout.simple_list_item_1))
        brandAutoComplete.threshold = 1


        seatsAutoComplete.setAdapter(ArrayAdapter.createFromResource(
            this,
            R.array.seat_array,
            android.R.layout.simple_list_item_1))
        seatsAutoComplete.threshold = 1

        colorsAutoComplete.setAdapter(ArrayAdapter.createFromResource(
            this,
            R.array.color_array,
            android.R.layout.simple_list_item_1))
        colorsAutoComplete.threshold = 1


    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}