package com.notification.fcm.receiver


import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.notification.fcm.FcmPluginBackground
import io.flutter.embedding.engine.FlutterShellArgs
import java.util.*


class BackgroundReceiver : JobIntentService(){


    companion object{


        var TAG = "FLTFireMsgService"
        var messagingQueue = Collections.synchronizedList(LinkedList<Intent>())
        /** Background Dart execution context.  */
        var flutterBackgroundExecutor: FcmPluginBackground? = null



        fun onInitialized() {
            Log.i(BackgroundReceiver.TAG, "FlutterFirebaseMessagingBackgroundService started!")
            synchronized(BackgroundReceiver.messagingQueue) {
                for (intent in BackgroundReceiver.messagingQueue) {
                    flutterBackgroundExecutor?.executeDartCallbackInBackgroundIsolate(
                        intent,
                        null
                    )
                }
                messagingQueue.clear()
            }
        }



        fun startBackgroundIsolate(callbackHandle: Long, shellArgs: FlutterShellArgs?) {
            if (flutterBackgroundExecutor != null) {
                Log.w(TAG, "Attempted to start a duplicate background isolate. Returning...")
                return
            }
            flutterBackgroundExecutor = FcmPluginBackground()
            flutterBackgroundExecutor?.startBackgroundIsolate(callbackHandle, shellArgs)
        }

    }


    override fun onHandleWork(intent: Intent) {

    }


    override fun onCreate() {
        super.onCreate()
    }


}