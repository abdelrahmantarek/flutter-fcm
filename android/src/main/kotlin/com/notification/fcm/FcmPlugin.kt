package com.notification.fcm

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import com.notification.fcm.helper.ContextHolder
import com.notification.fcm.helper.Utils
import com.notification.fcm.notification.NotificationC
import com.notification.fcm.receiver.BackgroundReceiver
import com.notification.fcm.receiver.OnMessageReceived
import io.flutter.embedding.engine.FlutterShellArgs
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import java.util.*


/** FcmPlugin */
class FcmPlugin: FlutterPlugin, MethodChannel.MethodCallHandler, PluginRegistry.NewIntentListener, ActivityAware{
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var activity : Activity
  private lateinit var flutterPluginBinding : FlutterPlugin.FlutterPluginBinding



  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
   this.flutterPluginBinding = flutterPluginBinding
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "fcm")
    channel.setMethodCallHandler(this)
  }


  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }


  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    this.activity = binding.activity
    binding.addOnNewIntentListener(this)
    ContextHolder.applicationContext = binding.activity.applicationContext
  }



  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    binding.addOnNewIntentListener(this)
    this.activity = binding.activity
  }


  override fun onDetachedFromActivityForConfigChanges() {

  }


  override fun onDetachedFromActivity() {

  }




  @SuppressLint("LongLogTag")
  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
    when (call.method) {

      "getPlatformVersion" -> {
        result.success("FcmPlugin ${android.os.Build.VERSION.RELEASE}")
        OnMessageReceived.firebase().token.addOnCompleteListener { task ->
          if (!task.isSuccessful) {
            Log.d("MyFirebaseMessaging", "Fetching FCM registration token failed ${task.exception}")
          }
          // Get new FCM registration token
          val token = task.result
          Log.d(OnMessageReceived.TAG, "token : $token")
        }
      }

      "getToken" -> {
        OnMessageReceived.firebase().token.addOnCompleteListener { task ->
          if (!task.isSuccessful) {
            Log.d("MyFirebaseMessaging", "Fetching FCM registration token failed ${task.exception}")
          }
          // Get new FCM registration token
          val token = task.result
          result.success(token)
        }
      }

      "deleteToken" -> {
        deleteToken()
        result.success(true)
      }

      "show_notification" -> {
        val value = call.arguments as Map<String, Any>
        NotificationC().showNotification(value.getValue("title").toString(),value.getValue("body").toString(),value)
        result.success(true)
      }

      "startListener" -> {

        activity.intent.action = OnMessageReceived.ACTION_CLICK;
        onNewIntent(activity.intent)


        // start background
        BackgroundReceiver.startBackgroundIsolate(1, FlutterShellArgs.fromIntent(activity.getIntent()))


        // add icons notification
        val value = call.arguments as Map<String, Any>
        var androidSettings = value.getValue("android_settings") as Map<String,Any>
        Utils.setIcon(androidSettings.getValue("icon").toString(),androidSettings.getValue("resource").toString())
        result.success(true)
      }

      "subscribeToTopic" -> {
        // add icons notification
        val value = call.arguments as Map<String, Any>
        FirebaseMessaging.getInstance().subscribeToTopic(value.getValue("topic") as String)
      }

      "unsubscribeFromTopic" -> {
        // add icons notification
        val value = call.arguments as Map<String, Any>
        FirebaseMessaging.getInstance().unsubscribeFromTopic(value.getValue("topic") as String)
      }

      else -> {
        result.notImplemented()
      }
    }
  }






  private fun deleteToken(){
    FirebaseMessaging.getInstance().deleteToken()
  }





  @SuppressLint("LongLogTag")
  override fun onNewIntent(intent: Intent?): Boolean {
    Log.d(OnMessageReceived.TAG, "onReceive action : " + intent?.action.toString())
    val sharedPreference = activity.getSharedPreferences(OnMessageReceived.NOTIFICATION, Context.MODE_PRIVATE)
    when (intent?.action) {

      OnMessageReceived.ACTION_ON_MESSAGE -> {
        if(sharedPreference?.getString("data",null) == null){
          return true
        }
        channel.invokeMethod("on_message",sharedPreference.getString("data",null))
      }

      OnMessageReceived.ACTION_CLICK -> {
        if(intent?.getStringExtra("data") == null){
          return true
        }
        channel.invokeMethod("on_click_notification",intent.getStringExtra("data"))
      }

    }
    return true
  }





}
