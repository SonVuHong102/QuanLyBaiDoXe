package com.zagon102.quanlybaidoxe

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MenuActivity : AppCompatActivity() {
    private lateinit var accountButton: View
    private lateinit var reportButton: View
    private lateinit var vehicleInButton: View
    private lateinit var vehicleOutButton: View
    private lateinit var logoutButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        initViews()
    }

    private fun initViews() {
        hideButton()
        accountButton = findViewById(R.id.account_button)
        reportButton = findViewById(R.id.report_button)
        vehicleInButton = findViewById(R.id.vehicle_in_button)
        vehicleOutButton = findViewById(R.id.vehicle_out_button)
        logoutButton = findViewById(R.id.logout_button)

        accountButton.setOnClickListener{
            goToAccount()
        }

        reportButton.setOnClickListener{
            goToReport()
        }

        vehicleInButton.setOnClickListener{
            goToVehicleIn()
        }

        vehicleOutButton.setOnClickListener{
            goToVehicleOut()
        }

        logoutButton.setOnClickListener{
            logout()
        }
    }

    private fun goToAccount() {
        val intent = Intent(this,AccountActivity::class.java)
        intent.putExtra(Constants.TYPE,Constants.INFO)
        startActivity(intent)
    }

    private fun goToReport() {
        startActivity(Intent(this,ReportActivity::class.java))
    }

    private fun goToVehicleIn() {
        startActivity(Intent(this,VehicleInActivity::class.java))
    }

    private fun goToVehicleOut() {
        startActivity(Intent(this,VehicleOutActivity::class.java))
    }

    private fun logout() {
        val intent = Intent(this,LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        UserInfoModule.user = null
        finish()
        startActivity(intent)
    }
}