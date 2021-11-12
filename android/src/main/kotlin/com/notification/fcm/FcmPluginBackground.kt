package com.notification.fcm


import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.AssetManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.notification.fcm.helper.ContextHolder
import com.notification.fcm.receiver.BackgroundReceiver
import com.notification.fcm.receiver.OnMessageReceived.Companion.TAG
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterShellArgs
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.dart.DartExecutor.DartCallback
import io.flutter.embedding.engine.loader.FlutterLoader
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.view.FlutterCallbackInformation
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean




class FcmPluginBackground : MethodChannel.MethodCallHandler {

    private val isCallbackDispatcherReady = AtomicBoolean(false)


    private var backgroundChannel: MethodChannel? = null
    private var backgroundFlutterEngine: FlutterEngine? = null



    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

    }

    /**
     * Returns true when the background isolate has started and is ready to handle background
     * messages.
     */
    fun isNotRunning(): Boolean {
        return !isCallbackDispatcherReady.get()
    }

    private fun onInitialized() {
        isCallbackDispatcherReady.set(true)
        BackgroundReceiver.onInitialized()
    }

    @SuppressLint("LongLogTag")
    fun startBackgroundIsolate(callbackHandle: Long, shellArgs: FlutterShellArgs?) {
//        if (backgroundFlutterEngine != null) {
//            Log.e(TAG, "Background isolate already started.")
//            return
//        }
//        val loader = FlutterLoader()
//        val mainHandler = Handler(Looper.getMainLooper())
//        val myRunnable = Runnable {
//            loader.startInitialization(ContextHolder.applicationContext!!)
//            loader.ensureInitializationCompleteAsync(
//                ContextHolder.applicationContext!!,
//                null,
//                mainHandler
//            ) {
//                val appBundlePath = loader.findAppBundlePath()
//                val assets: AssetManager = ContextHolder.applicationContext!!.assets
//                if (isNotRunning()) {
//                    if (shellArgs != null) {
//                        Log.i(TAG, "Creating background FlutterEngine instance, with args: " + Arrays.toString(shellArgs.toArray()))
//                        backgroundFlutterEngine = FlutterEngine(ContextHolder.applicationContext!!, shellArgs.toArray())
//                    } else {
//                        Log.i(TAG, "Creating background FlutterEngine instance.")
//                        backgroundFlutterEngine = FlutterEngine(ContextHolder.applicationContext!!)
//                    }
//                    // We need to create an instance of `FlutterEngine` before looking up the
//                    // callback. If we don't, the callback cache won't be initialized and the
//                    // lookup will fail.
//                    val flutterCallback = FlutterCallbackInformation.lookupCallbackInformation(callbackHandle)
//                    val executor: DartExecutor = backgroundFlutterEngine!!.dartExecutor
//                    initializeMethodChannel(executor)
//                    val dartCallback = DartCallback(assets, appBundlePath, flutterCallback)
//                    executor.executeDartCallback(dartCallback)
//                }
//            }
//        }
//        mainHandler.post(myRunnable)
    }


    private fun initializeMethodChannel(isolate: BinaryMessenger) {
        backgroundChannel = MethodChannel(isolate, "fcm/background")
        backgroundChannel?.setMethodCallHandler(this)
    }

    @SuppressLint("LongLogTag")
    fun executeDartCallbackInBackgroundIsolate(intent: Intent, latch: CountDownLatch?) {
//        if (backgroundFlutterEngine == null) {
//            Log.i(
//                FlutterFirebaseMessagingBackgroundExecutor.TAG,
//                "A background message could not be handled in Dart as no onBackgroundMessage handler has been registered."
//            )
//            return
//        }
//        var result: MethodChannel.Result? = null
//        if (latch != null) {
//            result = object : MethodChannel.Result {
//                override fun success(result: Any?) {
//                    // If another thread is waiting, then wake that thread when the callback returns a result.
//                    latch.countDown()
//                }
//
//                override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {
//                    latch.countDown()
//                }
//
//                override fun notImplemented() {
//                    latch.countDown()
//                }
//            }
//        }
//
//        // Handle the message event in Dart.
//        val remoteMessage: RemoteMessage = intent.getParcelableExtra(FlutterFirebaseMessagingUtils.EXTRA_REMOTE_MESSAGE)
//        if (remoteMessage != null) {
//            val remoteMessageMap: Map<String, Any> = FlutterFirebaseMessagingUtils.remoteMessageToMap(remoteMessage)
//            backgroundChannel!!.invokeMethod("MessagingBackground#onMessage", object : HashMap<String?, Any?>() {
//                    init {
//                        put("userCallbackHandle", getUserCallbackHandle())
//                        put("message", remoteMessageMap)
//                    }
//                },
//                result
//            )
//        } else {
//            Log.e(TAG, "RemoteMessage instance not found in Intent.")
//        }
    }

}