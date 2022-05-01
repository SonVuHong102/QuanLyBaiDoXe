package com.zagon102.quanlybaidoxe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import kotlin.concurrent.schedule

class LoginActivity : AppCompatActivity() {
    lateinit var usernameText: TextInputEditText
    lateinit var passwordText: TextInputEditText
    lateinit var errorText: TextView
    lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }

    private fun initView() {
        usernameText = findViewById(R.id.text_username)
        passwordText = findViewById(R.id.text_password)
        errorText = findViewById(R.id.errorText)
        loginButton = findViewById(R.id.button_login)

        usernameText.addTextChangedListener{
            errorText.text = ""
        }

        passwordText.addTextChangedListener{
            errorText.text = ""
        }

        passwordText.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                loginButton.performClick()
                true
            } else {
                false
            }
        }

        loginButton.setOnClickListener{
            showLoading()
            if(validateInput())
                startActivity(Intent(this,MenuActivity::class.java))
            else
                errorText.text = getString(R.string.login_hint)
            hideLoading()
        }
    }

    private fun validateInput(): Boolean {
        return usernameText.text?.isNotBlank() ?: false && passwordText.text?.isNotBlank() ?: false
    }


}