package com.notification.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage



class MyFirebaseMessagingService : FirebaseMessagingService() {


    private val TAG: String = "MyFirebaseMessagingService"


    companion object{
        val TAG: String = "MyFirebaseMessagingService"
        val NOTIFICATION: String = "NOTIFICATION"
        val ACTION_CLICK: String = "action.CLICK_NOTIFICATION"
        fun firebase(): FirebaseMessaging {
            return FirebaseMessaging.getInstance();
        }
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            // Compose and show notification
            if (!remoteMessage.data.isNullOrEmpty()) {
                val title: String = remoteMessage.data.get("title").toString()
                val body: String = remoteMessage.data.get("body").toString()
                val toScreen: String = remoteMessage.data.get("navigate_to_screen").toString()
                sendNotification(title,body,toScreen,remoteMessage.data)
            }
        }
    }


    // TODO: Step 3.2 log registration token
    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * InstanceID token is initially generated so this is where you would retrieve
     * the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]


    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG, "Navigator To : ${this.packageName+".MainActivity"}")
    }


    private fun sendNotification(title: String?,body: String?,to_screen: String?,data:Any) {

        Log.d(TAG, "Navigator To : ${this.packageName+".MainActivity"}")

        val resId = resources.getIdentifier("ic_launcher".split("\\.").get(0), "mipmap", applicationInfo.packageName)

        val myIntent = Intent(this, BroadCastClickNotification::class.java)
//          myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        myIntent.putExtra("navigate_to_screen",to_screen);
        myIntent.putExtra("data",data.toString());
        saveData(data.toString());

        val pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent,  0)
//      val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(resId)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        // https://developer.android.com/training/notify-user/build-notification#Priority
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }


    fun saveData(data: String){
        val sharedPreference =  getSharedPreferences(NOTIFICATION,Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("data",data)
        editor.apply()
    }


}