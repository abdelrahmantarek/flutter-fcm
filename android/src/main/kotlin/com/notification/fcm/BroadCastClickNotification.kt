package com.notification.fcm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log



class BroadCastClickNotification :BroadcastReceiver() {

    override fun onReceive(context: Context?, reciver: Intent?) {

        Log.d("event_listener","new notification");
        val intent = Intent().setClassName(context!!,context.packageName+".MainActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("navigate_to_screen",reciver?.getStringExtra("navigate_to_screen"));
        intent.putExtra("data",reciver?.getStringExtra("data"));
        context.startActivity(intent)

        FcmPlugin.onNewNotification(intent);

    }

}

