package com.zagon102.quanlybaidoxe.ultis

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.children
import com.zagon102.quanlybaidoxe.R
import com.zagon102.quanlybaidoxe.presentation.module.UserInfoModule
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Activity.showLoading() {
    val progress = findViewById<View>(R.id.loadingView)
    if(progress != null) {
        progress.visibility = View.VISIBLE
        window?.decorView?.rootView?.setAllEnabled(false)
    }
}

fun Activity.hideLoading() {
    val progress = findViewById<View>(R.id.loadingView)
    if(progress != null) {
        progress.visibility = View.GONE
        window?.decorView?.rootView?.setAllEnabled(true)
    }
}

fun View.setAllEnabled(enabled: Boolean) {
    isEnabled = enabled
    if (this is ViewGroup) children.forEach { child -> child.setAllEnabled(enabled) }
}

fun Activity.hideButton() {
    val windowInsetsController =
        ViewCompat.getWindowInsetsController(window.decorView) ?: return
    // Configure the behavior of the hidden system bars
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    // Hide both the status bar and the navigation bar
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
}

fun LocalDate.toDateFormat(): String {
    return format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

fun Long.toCurrency(): String {
    val dec = DecimalFormat("###,###,###")
    return dec.format(this) + " VND"
}

fun String.toLocalDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return LocalDate.parse(this, formatter)
}

fun Context.localStorage() : SharedPreferences? {
    return getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,MODE_PRIVATE)
}

fun Context.saveUserToLocal() {
    localStorage()?.edit()?.let {
        it.putString(Constants.USER,UserInfoModule.user?.username ?: "")
        it.putString(Constants.PASSWORD,UserInfoModule.user?.password ?: "")
        it.apply()
    }
}

fun String.isValidEmail(): Boolean {
    return !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}