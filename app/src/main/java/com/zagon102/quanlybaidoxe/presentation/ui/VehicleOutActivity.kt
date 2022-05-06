package com.zagon102.quanlybaidoxe.presentation.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.zagon102.quanlybaidoxe.*
import com.zagon102.quanlybaidoxe.data.DBHelper
import com.zagon102.quanlybaidoxe.presentation.model.VehicleCheck
import com.zagon102.quanlybaidoxe.presentation.ui.adapter.VehicleListAdapter
import com.zagon102.quanlybaidoxe.ultis.Constants
import com.zagon102.quanlybaidoxe.ultis.toCurrency
import com.zagon102.quanlybaidoxe.ultis.toDateFormat
import com.zagon102.quanlybaidoxe.ultis.toLocalDate
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class VehicleOutActivity : AppCompatActivity() {
    private lateinit var vehicleCheckListView: RecyclerView
    private lateinit var backButton: Button
    private lateinit var adapter: VehicleListAdapter
    private lateinit var vehicleCheckList: MutableList<VehicleCheck>
    private var db: DBHelper? = null
    private var vehicleCheck: VehicleCheck? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_out)
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
        adapter = VehicleListAdapter(vehicleCheckList,false,onItemClick)
        vehicleCheckListView.adapter = adapter
    }

    private fun getData() {
        vehicleCheckList.clear()
        val cursor = db?.getPendingChecks()
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
                        null,
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NAME_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PHONE_COL)),
                        null,
                        "1"
                ))
            } while (cursor.moveToNext())
        } else {
            Toast.makeText(this,"Empty",Toast.LENGTH_SHORT).show()
        }
        adapter.notifyDataSetChanged()
    }

    private val onItemClick: (VehicleCheck) -> Unit = {
        showDialog(it)
    }

    private fun showDialog(vehicleCheck: VehicleCheck) {
        this.vehicleCheck = vehicleCheck
        this.vehicleCheck!!.checkOutDate = LocalDate.now()
        this.vehicleCheck!!.cash = (ChronoUnit.DAYS.between(vehicleCheck.checkInDate, LocalDate.now())* Constants.price)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_checkout)
        dialog.findViewById<TextView>(R.id.text_brand).text = this.vehicleCheck!!.brand
        dialog.findViewById<TextView>(R.id.text_seats).text = this.vehicleCheck!!.seats.toString()
        dialog.findViewById<TextView>(R.id.text_color).text = this.vehicleCheck!!.color
        dialog.findViewById<TextView>(R.id.text_plate).text = this.vehicleCheck!!.plate
        dialog.findViewById<TextView>(R.id.text_checkin_date).text = this.vehicleCheck!!.checkInDate.toDateFormat()
        dialog.findViewById<TextView>(R.id.text_checkout_date).text = this.vehicleCheck!!.checkOutDate!!.toDateFormat()
        dialog.findViewById<TextView>(R.id.text_name).text = this.vehicleCheck!!.name
        dialog.findViewById<TextView>(R.id.text_phone).text = this.vehicleCheck!!.phone
        dialog.findViewById<TextView>(R.id.text_cash).text = this.vehicleCheck!!.cash!!.toCurrency()
        val yesBtn = dialog.findViewById<Button>(R.id.button_checkout)
        val noBtn = dialog.findViewById<Button>(R.id.button_cancel)

        yesBtn.setOnClickListener {
            dialog.dismiss()
            this.vehicleCheck?.let{
                it.done = Constants.DONE
            }
            db?.updateCheck(vehicleCheck)
            val bundle =  Bundle()
            bundle.putSerializable(Constants.VEHICLE_CHECK,vehicleCheck)
            val intent = Intent(this, CheckOutCompleteActivity::class.java)
            intent.putExtra(Constants.BUNDLE,bundle)
            startActivity(intent)
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}