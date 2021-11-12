package com.notification.fcm.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.notification.fcm.helper.ContextHolder
import com.notification.fcm.receiver.OnMessageReceived
import com.notification.fcm.actions.Receiver
import org.json.JSONObject

class NotificationC {


    @SuppressLint("LongLogTag")
    fun showNotification(title: String?, body: String?, data:Map<String,Any>) {

        val resId = ContextHolder.applicationContext?.resources?.getIdentifier("ic_launcher".split("\\.").get(0), "mipmap", ContextHolder.applicationContext?.packageName)

        val myIntent = Intent(ContextHolder.applicationContext, Receiver::class.java)
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        myIntent.putExtra("data","${JSONObject(data)}")


        val pendingIntent = PendingIntent.getBroadcast(ContextHolder.applicationContext, 0, myIntent,  0)
//      val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(ContextHolder.applicationContext!!, channelId)
            .setSmallIcon(resId!!)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = ContextHolder.applicationContext?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        // https://developer.android.com/training/notify-user/build-notification#Priority
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())

    }


}