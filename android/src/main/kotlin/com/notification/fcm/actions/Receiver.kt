package com.notification.fcm.actions

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.notification.fcm.receiver.OnMessageReceived


class Receiver : BroadcastReceiver() {

    @SuppressLint("LongLogTag")
    override fun onReceive(context: Context?, receiver: Intent?) {

        if(receiver?.action == null){
            Log.d(TAG, "Receiver : ${receiver?.action}")
            val sharedPreference = context?.getSharedPreferences(OnMessageReceived.NOTIFICATION,Context.MODE_PRIVATE)
            val intent = Intent().setClassName(context!!,context.packageName+".MainActivity")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = receiver?.action
            intent.putExtra("data",sharedPreference?.getString("data","null"))
            context.startActivity(intent)

        }

    }

}