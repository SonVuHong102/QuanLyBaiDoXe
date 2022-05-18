package com.zagon102.quanlybaidoxe.presentation.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.zagon102.quanlybaidoxe.*
import com.zagon102.quanlybaidoxe.data.DBHelper
import com.zagon102.quanlybaidoxe.presentation.model.User
import com.zagon102.quanlybaidoxe.presentation.module.UserInfoModule
import com.zagon102.quanlybaidoxe.ultis.*
import java.time.LocalDate
import java.util.*

class AccountActivity : AppCompatActivity() {
    private lateinit var mainTitle: TextView
    private lateinit var usernameText: TextInputEditText
    private lateinit var passwordText: TextInputEditText
    private lateinit var rePasswordText: TextInputEditText
    private lateinit var errorText: TextView
    private lateinit var nameText: TextInputEditText
    private lateinit var dobText: TextInputEditText
    private lateinit var buttonDatePicker: ImageButton
    private lateinit var phoneText: TextInputEditText
    private lateinit var emailText: TextInputEditText
    private lateinit var editAccountButton: Button
    private lateinit var createAccountButton: Button

    private lateinit var db: DBHelper

    private var role = Constants.CUSTOMER
    private var type = Constants.INFO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        db = DBHelper(this, null)
        getData()
        initViews()
    }

    private fun getData() {
        role = UserInfoModule.user?.role ?: Constants.CUSTOMER
        type = intent.getStringExtra(Constants.TYPE) ?: Constants.INFO
    }

    override fun onResume() {
        if(type != Constants.CREATE)
             getUserInfo()
        super.onResume()
    }

    private fun initViews() {
        hideButton()
        mainTitle = findViewById(R.id.mainTitle)
        usernameText = findViewById(R.id.text_username)
        passwordText = findViewById(R.id.text_password)
        rePasswordText = findViewById(R.id.text_repassword)
        errorText = findViewById(R.id.errorText)
        nameText = findViewById(R.id.text_name)
        dobText = findViewById(R.id.text_dob)
        buttonDatePicker = findViewById(R.id.button_datepicker)
        phoneText = findViewById(R.id.text_phone)
        emailText = findViewById(R.id.text_email)
        editAccountButton = findViewById(R.id.button_edit_account)

        when (type) {
            Constants.INFO -> showInfoState()
            Constants.CREATE -> showCreateAccountState()
            Constants.EDIT -> showEditAccountState()
        }

        buttonDatePicker.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                val date = LocalDate.of(year,monthOfYear+1,dayOfMonth)
                dobText.setText(date.toDateFormat())
            }, year, month, day)
            dpd.datePicker.maxDate = Date().time
            dpd.show()
            dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        }

        editAccountButton.setOnClickListener {
            when (type) {
                Constants.EDIT -> if (validatePassword()) {
                    UserInfoModule.user?.let {
                        it.password = passwordText.text.toString()
                        it.name = nameText.text.toString()
                        it.dob = dobText.text.toString().toLocalDate()
                        it.phone = phoneText.text.toString()
                        it.email = emailText.text.toString()
                        if(!it.email.isValidEmail()) {
                            errorText.text = getString(R.string.email_not_valid)
                            Toast.makeText(this,getString(R.string.email_not_valid),Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        db.updateUser(it)
                        finish()
                    }
                } else {
                    errorText.text = getString(R.string.password_mismatch)
                    Toast.makeText(this,getString(R.string.password_mismatch),Toast.LENGTH_SHORT).show()

                }
                Constants.CREATE -> if(validateUsername() && validatePassword()) {
                    if(!emailText.text.toString().isValidEmail()) {
                        errorText.text = getString(R.string.email_not_valid)
                        Toast.makeText(this,getString(R.string.email_not_valid),Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val cursor = db?.getUser(usernameText.text.toString())
                    if(cursor != null && cursor.moveToFirst()) {
                        errorText.text = getString(R.string.username_not_availabel)
                        Toast.makeText(this,getString(R.string.username_not_availabel),Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val user = User(
                        null,
                        usernameText.text.toString(),
                        passwordText.text.toString(),
                        if(role == Constants.MANAGER) Constants.EMPLOYEE else Constants.CUSTOMER,
                        nameText.text.toString(),
                        dobText.text.toString().toLocalDate(),
                        phoneText.text.toString(),
                        emailText.text.toString()
                    )
                    db.addUser(user)
                    if(role == Constants.CUSTOMER) {
                        UserInfoModule.user = user
                        saveUserToLocal()
                    }
                    finish()
                } else {
                    errorText.text = getString(R.string.create_user_error)
                    Toast.makeText(this,getString(R.string.create_user_error),Toast.LENGTH_SHORT).show()
                }
                Constants.INFO -> goToEditAccount()
            }
        }
    }

    private fun validateUsername(): Boolean {
        return db.getUser(usernameText.text.toString()) != null
    }

    private fun validatePassword(): Boolean {
        return passwordText.text.toString() == rePasswordText.text.toString()
    }

    private fun setFieldState(state: Boolean) {
        usernameText.isEnabled = state
        passwordText.isEnabled = state
        rePasswordText.isEnabled = state
        errorText.text = ""
        phoneText.isEnabled = state && type == Constants.CREATE
        nameText.isEnabled = state
        emailText.isEnabled = state
    }

    private fun showInfoState() {
        if (role == Constants.MANAGER) {
            showCreateButton()
        }
        findViewById<LinearLayout>(R.id.repassword_layout).visibility = View.GONE
        setFieldState(false)

    }

    private fun showCreateAccountState() {
        mainTitle.text = getString(R.string.create_account)
        editAccountButton.text = getString(R.string.create_new_account_label)
        findViewById<LinearLayout>(R.id.repassword_layout).visibility = View.VISIBLE
        buttonDatePicker.visibility = View.VISIBLE
        setFieldState(true)
        usernameText.setText("")
        passwordText.setText("")
        nameText.setText("")
        dobText.setText("")
        phoneText.setText("")
        emailText.setText("")

    }

    private fun showEditAccountState() {
        mainTitle.text = getString(R.string.edit_account)
        editAccountButton.text = getString(R.string.save)
        buttonDatePicker.visibility = View.VISIBLE
        findViewById<LinearLayout>(R.id.repassword_layout).visibility = View.VISIBLE
        setFieldState(true)
        usernameText.isEnabled = false
    }

    private fun getUserInfo() {
        usernameText.setText(UserInfoModule.user?.username ?: "")
        passwordText.setText(UserInfoModule.user?.password ?: "")
        nameText.setText(UserInfoModule.user?.name ?: "")
        dobText.setText(UserInfoModule.user?.dob?.toDateFormat() ?: "")
        phoneText.setText(UserInfoModule.user?.phone ?: "")
        emailText.setText(UserInfoModule.user?.email ?: "")
    }

    private fun goToEditAccount() {
        val intent = Intent(this, this::class.java)
        intent.putExtra(Constants.TYPE, Constants.EDIT)
        startActivity(intent)
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
        val intent = Intent(this, this::class.java)
        intent.putExtra(Constants.TYPE, Constants.CREATE)
        startActivity(intent)
    }
}