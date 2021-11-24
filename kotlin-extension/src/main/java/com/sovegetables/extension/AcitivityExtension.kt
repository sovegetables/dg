package com.sovegetables.extension

import android.content.Context
import android.os.Handler
import com.sovegetables.utils.ToastHelper

fun Context.toast(content: String?){
    ToastHelper.ShowToast(content, this)
}

fun postDelay(runnable: Runnable, delay: Long){
    Handler().postDelayed(runnable, delay)
}