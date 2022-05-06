package com.zagon102.quanlybaidoxe.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.zagon102.quanlybaidoxe.*
import com.zagon102.quanlybaidoxe.presentation.model.VehicleCheck
import com.zagon102.quanlybaidoxe.ultis.Constants
import com.zagon102.quanlybaidoxe.ultis.toCurrency
import com.zagon102.quanlybaidoxe.ultis.toDateFormat
import java.time.temporal.ChronoUnit

class CheckOutCompleteActivity : AppCompatActivity() {
    lateinit var plateText: TextView
    lateinit var checkinText: TextView
    lateinit var checkoutText: TextView
    lateinit var nameText: TextView
    lateinit var phoneText: TextView
    lateinit var cashText: TextView
    lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out_complete)

        initViews()
        getData()

    }

    private fun initViews() {
        plateText = findViewById(R.id.text_plate)
        checkinText = findViewById(R.id.text_checkin_date)
        checkoutText = findViewById(R.id.text_checkout_date)
        nameText = findViewById(R.id.text_name)
        phoneText = findViewById(R.id.text_phone)
        cashText = findViewById(R.id.text_cash)
        backButton = findViewById(R.id.button_back)

        backButton.setOnClickListener{
            finish()
        }
    }

    private fun getData() {
        val bundle = intent.getBundleExtra(Constants.BUNDLE)
        bundle?.let {
            val vehicleCheck = bundle.getSerializable(Constants.VEHICLE_CHECK) as VehicleCheck?
            vehicleCheck?.let {
                plateText.text = it.plate
                checkinText.text = it.checkInDate.toDateFormat()
                checkoutText.text = it.checkOutDate!!.toDateFormat()
                nameText.text = it.name
                phoneText.text = it.phone
                cashText.text = it.cash!!.toCurrency()
            }
        }
    }
}