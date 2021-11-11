package com.notification.fcm

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import com.google.firebase.messaging.FirebaseMessaging
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry



/** FcmPlugin */
class FcmPlugin: BroadcastReceiver() , FlutterPlugin, MethodChannel.MethodCallHandler, PluginRegistry.NewIntentListener, ActivityAware, EventChannel.StreamHandler{
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var activity : Activity
  private lateinit var _stream : EventChannel
  private lateinit var flutterPluginBinding : FlutterPlugin.FlutterPluginBinding
  private var snik : EventChannel.EventSink? = null


  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
   this.flutterPluginBinding = flutterPluginBinding
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "fcm")
    channel.setMethodCallHandler(this)
    _stream = EventChannel(flutterPluginBinding.binaryMessenger,"notification.eventchannel.sample/stream")
    _stream.setStreamHandler(this)
  }


  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }


  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    this.activity = binding.activity
    binding.addOnNewIntentListener(this)
    resumeNotification()
  }


  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    binding.addOnNewIntentListener(this)
    this.activity = binding.activity
  }


  override fun onDetachedFromActivityForConfigChanges() {

  }


  override fun onDetachedFromActivity() {

  }


  override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
    snik = events
    snik?.success(activity.intent.getStringExtra("data"))
  }


  override fun onCancel(arguments: Any?) {
    snik?.endOfStream()
    snik = null
  }


  @SuppressLint("LongLogTag")
  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
    when (call.method) {


      "getPlatformVersion" -> {
        result.success("FcmPlugin ${android.os.Build.VERSION.RELEASE}")
        MyFirebaseMessagingService.firebase().token.addOnCompleteListener { task ->
          if (!task.isSuccessful) {
            Log.d("MyFirebaseMessaging", "Fetching FCM registration token failed ${task.exception}")
          }
          // Get new FCM registration token
          val token = task.result
          Log.d(MyFirebaseMessagingService.TAG, "token : $token")
        }
      }


      "getToken" -> {
        MyFirebaseMessagingService.firebase().token.addOnCompleteListener { task ->
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

      "pause_notification" -> {
        pauseNotification()
        result.success(true)
      }

      "resume_notification" -> {
        resumeNotification()
        result.success(true)
      }
      else -> {
        result.notImplemented()
      }
    }
  }

   private fun pauseNotification() {
    val sharedPreference = activity.getSharedPreferences(MyFirebaseMessagingService.NOTIFICATION,Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()
    editor.putBoolean(MyFirebaseMessagingService.SHOW_NOTIFICATION_KEY,false)
    editor.apply()
  }

   private fun resumeNotification() {
    val sharedPreference = activity.getSharedPreferences(MyFirebaseMessagingService.NOTIFICATION,Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()
    editor.putBoolean(MyFirebaseMessagingService.SHOW_NOTIFICATION_KEY,true)
    editor.apply()
  }


  private fun deleteToken(){
    FirebaseMessaging.getInstance().deleteToken()
  }


  @SuppressLint("LongLogTag")
  override fun onReceive(context: Context?, receiver: Intent?) {
    val sharedPreference = context?.getSharedPreferences(MyFirebaseMessagingService.NOTIFICATION,Context.MODE_PRIVATE)
    val intent = Intent().setClassName(context!!,context.packageName+".MainActivity")
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.putExtra("data",sharedPreference?.getString("data","null"))
    context.startActivity(intent)
  }


  override fun onNewIntent(intent: Intent?): Boolean {
    Log.d("My App", intent?.getStringExtra("data").toString())
    snik?.success(intent?.getStringExtra("data"))
    return true
  }


}
