package com.notification.fcm.receiver

import android.app.Notification
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.notification.fcm.helper.ContextHolder
import com.notification.fcm.notification.NotificationC
import org.json.JSONObject


class OnMessageReceived : FirebaseMessagingService() {


    private val TAG: String = "MyFirebaseMessagingService"


    companion object{
        val TAG: String = "MyFirebaseMessagingService"
        val NOTIFICATION: String = "NOTIFICATION"
        val ACTION_CLICK: String? = null
        val ACTION_ON_MESSAGE: String = "com.google.android.c2dm.intent.RECEIVE"
        fun firebase(): FirebaseMessaging {
            return FirebaseMessaging.getInstance();
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        ContextHolder.applicationContext = this

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            // Compose and show notification
            if (!remoteMessage.data.isNullOrEmpty()) {
                saveNotification("${JSONObject(remoteMessage.data as Map<String,Any>)}")
                NotificationC().showNotification(remoteMessage.data.getValue("title"),remoteMessage.data.getValue("body"),remoteMessage.data);
            }
        }
    }

    fun saveNotification(data: String){
        val sharedPreference = ContextHolder.applicationContext?.getSharedPreferences(
            NOTIFICATION,Context.MODE_PRIVATE)
        val editor = sharedPreference?.edit()
        editor?.putString("data",data)
        editor?.apply()
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG, "Navigator To : ${this.packageName+".MainActivity"}")
    }

}

