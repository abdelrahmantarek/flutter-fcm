package com.notification.fcm

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.notification.fcm.MyFirebaseMessagingService.Companion.NOTIFICATION
import com.notification.fcm.MyFirebaseMessagingService.Companion.TAG


class BroadCastClickNotification :BroadcastReceiver() {



    @SuppressLint("LongLogTag")
    override fun onReceive(context: Context?, reciver: Intent?) {

        val sharedPreference = context?.getSharedPreferences(NOTIFICATION,Context.MODE_PRIVATE)

        val intent = Intent().setClassName(context!!,context.packageName+".MainActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("data",sharedPreference?.getString("data","null"));
        context.startActivity(intent)

        Log.d(TAG, "Message data payload: " + intent.getStringExtra("data"))

        FcmPlugin.onNewNotification(intent);

    }

}

