package com.zagon102.quanlybaidoxe

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.children

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