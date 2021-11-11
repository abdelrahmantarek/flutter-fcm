package com.notification.fcm

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel


/** FcmPlugin */
class FcmPlugin: FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware, EventChannel.StreamHandler{
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var activity : Activity
  private lateinit var _stream : EventChannel
  private lateinit var flutterPluginBinding : FlutterPlugin.FlutterPluginBinding



  companion object{

    var event_listener : EventChannel.EventSink? = null

    fun onNewNotification(intent: Intent) {
      event_listener?.success(intent.getStringExtra("data"))
    }

  }


  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
   this.flutterPluginBinding = flutterPluginBinding;
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
    Toast.makeText(activity, "start app", Toast.LENGTH_SHORT).show();
  }

  override fun onDetachedFromActivityForConfigChanges() {
    TODO("Not yet implemented")
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    TODO("Not yet implemented")
  }

  override fun onDetachedFromActivity() {
    TODO("Not yet implemented")
  }

  override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
    Log.d("event_listener","end");
    event_listener = events;
    if(activity.intent.hasExtra("data")){
       event_listener?.success(activity.intent.getStringExtra("data"))
    }
  }


  override fun onCancel(arguments: Any?) {
    event_listener?.endOfStream()
    event_listener = null;
    TODO("Not yet implemented")
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
      else -> {
        result.notImplemented()
      }
    }
  }

}
