package com.notification.fcm.helper

import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.Nullable
import com.notification.fcm.receiver.OnMessageReceived

class Utils {

    companion object{
        var sharedPreference : SharedPreferences? = null
        fun isApplicationForeground(context: Context): Boolean {
            val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (keyguardManager.isKeyguardLocked) {
                return false
            }
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                ?: return false
            val appProcesses = activityManager.runningAppProcesses ?: return false
            val packageName = context.packageName
            for (appProcess in appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName == packageName
                ) {
                    return true
                }
            }
            return false
        }

        fun getPreference():SharedPreferences{
            if(sharedPreference == null){
                sharedPreference = ContextHolder.applicationContext?.getSharedPreferences(OnMessageReceived.NOTIFICATION,Context.MODE_PRIVATE)
            }
            return sharedPreference!!
        }

        fun setString(key:String, @Nullable value :String){
            getPreference().edit()?.putString(key,value)?.apply();
        }

        fun getString(key:String, @Nullable defValue :String) :String{
            return getPreference().getString(key,defValue)!!
        }

        fun getIcon():String{
            return getString("icon","ic_launcher")
        }

        fun getResource():String{
            return getString("resource","mipmap")
        }

        fun setIcon(icon:String,resource:String){
            setString("icon",icon)
            setString("resource",resource)
        }

    }
}