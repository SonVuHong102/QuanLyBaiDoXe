package com.zagon102.quanlybaidoxe.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.zagon102.quanlybaidoxe.*
import com.zagon102.quanlybaidoxe.data.DBHelper
import com.zagon102.quanlybaidoxe.presentation.model.User
import com.zagon102.quanlybaidoxe.presentation.module.UserInfoModule
import com.zagon102.quanlybaidoxe.ultis.*
import java.time.LocalDate

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameText: TextInputEditText
    private lateinit var passwordText: TextInputEditText
    private lateinit var errorText: TextView
    private lateinit var loginButton: Button
    lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        getData()
        db = DBHelper(this, null)
    }

    private fun getData() {
        usernameText.setText(localStorage()?.getString(Constants.USER,""))
        passwordText.setText(localStorage()?.getString(Constants.PASSWORD,""))
    }

    private fun initViews() {
        hideButton()
        usernameText = findViewById(R.id.text_username)
        passwordText = findViewById(R.id.text_password)
        errorText = findViewById(R.id.errorText)
        loginButton = findViewById(R.id.button_login)

        usernameText.addTextChangedListener {
            errorText.text = ""
        }

        passwordText.addTextChangedListener {
            errorText.text = ""
        }

        passwordText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginButton.performClick()
                true
            } else {
                false
            }
        }

        loginButton.setOnClickListener {
            showLoading()
            if (validateInput() && authUser()) {
                val cursor = db.getUser(usernameText.text.toString())
                cursor!!.moveToFirst()
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.ID_COL))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NAME_COL))
                val dob = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DOB_COL))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PHONE_COL))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.EMAIL_COL))
                val role = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ROLE_COL))
                UserInfoModule.user = User(
                    id,
                    usernameText.text.toString(),
                    passwordText.text.toString(),
                    role,
                    name,
                    dob.toLocalDate(),
                    phone,
                    email
                )
                localStorage()?.edit()?.let {
                    it.putString(Constants.USER,usernameText.text.toString())
                    it.putString(Constants.PASSWORD,passwordText.text.toString())
                    it.apply()
                }
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            } else
                errorText.text = getString(R.string.login_hint)
            hideLoading()
        }
    }

    private fun validateInput(): Boolean {
        return usernameText.text?.isNotBlank() ?: false && passwordText.text?.isNotBlank() ?: false
    }

    private fun authUser(): Boolean {
        val username = usernameText.text.toString()
        val password = passwordText.text.toString()
        val cursor = db.authUser(username, password)
        if (cursor == null || !cursor.moveToFirst())
            return false
        return true
    }

    override fun finish() {
        db.close()
        super.finish()
    }
}