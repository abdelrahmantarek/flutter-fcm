package com.notification.fcm.helper

import android.content.Context
import android.util.Log


object ContextHolder {
    var applicationContext: Context? = null
        set(applicationContext) {
            Log.d("FLTFireContextHolder", "received application context.")
            field = applicationContext
        }
}
