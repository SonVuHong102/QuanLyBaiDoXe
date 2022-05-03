package com.zagon102.quanlybaidoxe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class AccountActivity : AppCompatActivity() {
    private lateinit var usernameText: TextInputEditText
    private lateinit var passwordText: TextInputEditText
    private lateinit var rePasswordText: TextInputEditText
    private lateinit var errorText: TextView
    private lateinit var nameText: TextInputEditText
    private lateinit var dobText: TextInputEditText
    private lateinit var phoneText: TextInputEditText
    private lateinit var emailText: TextInputEditText
    private lateinit var editAccountButton: Button
    private lateinit var createAccountButton: Button

    private var role = Constants.EMPLOYEE
    private var type = Constants.INFO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        getData()
        initViews()
    }

    private fun getData() {
        role = intent.getStringExtra(Constants.ROLE) ?: Constants.EMPLOYEE
        type = intent.getStringExtra(Constants.TYPE) ?: Constants.INFO
    }

    private fun initViews() {
        hideButton()
        usernameText = findViewById(R.id.text_username)
        passwordText = findViewById(R.id.text_password)
        rePasswordText = findViewById(R.id.text_repassword)
        errorText = findViewById(R.id.errorText)
        nameText = findViewById(R.id.text_name)
        dobText = findViewById(R.id.text_dob)
        phoneText = findViewById(R.id.text_phone)
        emailText = findViewById(R.id.text_email)
        editAccountButton = findViewById(R.id.button_edit_account)

        when(type) {
            Constants.INFO -> showInfoState()
            Constants.CREATE -> showCreateAccountState()
            Constants.EDIT -> showEditAccountState()
        }

        if(role == Constants.MANAGER) {
            showCreateButton()
        }

        editAccountButton.setOnClickListener {
            goToEditAccount()
        }
    }

    private fun setFieldState(state: Boolean) {
        usernameText.isEnabled = state
        passwordText.isEnabled = state
        rePasswordText.isEnabled = state
        errorText.text = ""
        nameText.isEnabled = state
        dobText.isEnabled = state
        phoneText.isEnabled = state
        emailText.isEnabled = state

    }

    private fun showInfoState() {
        findViewById<LinearLayout>(R.id.repassword_layout).visibility = View.GONE
        setFieldState(false)
        getUserInfo()
    }

    private fun showCreateAccountState() {
        findViewById<LinearLayout>(R.id.repassword_layout).visibility = View.VISIBLE
        setFieldState(true)
    }

    private fun showEditAccountState() {
        findViewById<LinearLayout>(R.id.repassword_layout).visibility = View.VISIBLE
        setFieldState(true)
        usernameText.isEnabled = false
    }

    private fun getUserInfo() {

    }

    private fun goToEditAccount() {
//        startActivity(Intent(this, EditAccountActivity::class.java))
    }

    private fun showCreateButton() {
        val buttonParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            resources.getDimension(R.dimen.buttonHeight).toInt(),
            1.0f
        )
        editAccountButton.layoutParams = buttonParam
        createAccountButton = findViewById(R.id.button_create_account)
        createAccountButton.layoutParams = buttonParam
        createAccountButton.visibility = View.VISIBLE
        createAccountButton.setOnClickListener {
            goToCreateAccount()
        }
    }


    private fun goToCreateAccount() {
//        startActivity(Intent(this, CreateAccountActivity::class.java))
    }
}