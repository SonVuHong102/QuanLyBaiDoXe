package com.zagon102.quanlybaidoxe.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.zagon102.quanlybaidoxe.*
import com.zagon102.quanlybaidoxe.data.DBHelper
import com.zagon102.quanlybaidoxe.presentation.model.User
import com.zagon102.quanlybaidoxe.presentation.module.UserInfoModule
import com.zagon102.quanlybaidoxe.ultis.Constants
import com.zagon102.quanlybaidoxe.ultis.hideButton
import com.zagon102.quanlybaidoxe.ultis.toDateFormat
import com.zagon102.quanlybaidoxe.ultis.toLocalDate

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

    private lateinit var db: DBHelper

    private var role = Constants.EMPLOYEE
    private var type = Constants.INFO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        db = DBHelper(this, null)
//        showLoading()
        getData()
        initViews()
//        hideLoading()
    }

    private fun getData() {
        role = UserInfoModule.user?.role ?: Constants.EMPLOYEE
        type = intent.getStringExtra(Constants.TYPE) ?: Constants.INFO
    }

    override fun onResume() {
        if(type != Constants.CREATE)
             getUserInfo()
        super.onResume()
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

        when (type) {
            Constants.INFO -> showInfoState()
            Constants.CREATE -> showCreateAccountState()
            Constants.EDIT -> showEditAccountState()
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
                        db.updateUser(it)
                        finish()
                    }
                } else {
                    errorText.text = getString(R.string.password_mismatch)
                }
                Constants.CREATE -> if(validateUsername() && validatePassword()) {
                    db.addUser(
                        User(
                            null,
                            usernameText.text.toString(),
                            passwordText.text.toString(),
                            Constants.EMPLOYEE,
                            nameText.text.toString(),
                            dobText.text.toString().toLocalDate(),
                            phoneText.text.toString(),
                            emailText.text.toString()
                        )
                    )
                    finish()
                } else {
                    errorText.text = "Username is exist or Passwords mismatch"
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
        nameText.isEnabled = state
        dobText.isEnabled = state
        phoneText.isEnabled = state
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
        editAccountButton.text = getString(R.string.create_new_account_label)
        findViewById<LinearLayout>(R.id.repassword_layout).visibility = View.VISIBLE
        setFieldState(true)
        usernameText.setText("")
        passwordText.setText("")
        nameText.setText("")
        dobText.setText("")
        phoneText.setText("")
        emailText.setText("")

    }

    private fun showEditAccountState() {
        editAccountButton.text = getString(R.string.save)
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