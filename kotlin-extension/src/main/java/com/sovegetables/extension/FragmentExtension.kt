package com.sovegetables.extension

import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment
import com.sovegetables.utils.ToastHelper


fun Fragment.toast(content: String?) {
    return ToastHelper.ShowToast(content, activity)
}

fun isNetworkConnected(context: Context?): Boolean {
    return if (context == null) {
        false
    } else {
        try {
            val networkInfo = (context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        } catch (var2: Exception) {
            false
        }
    }
}