import Flutter
import UIKit
import FirebaseMessaging
import FirebaseCore



public class SwiftFcmPlugin: NSObject, FlutterPlugin,MessagingDelegate {
    
    
    
  var channel: FlutterMethodChannel?
    
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "fcm", binaryMessenger: registrar.messenger())
    let instance = SwiftFcmPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
    registrar.addApplicationDelegate(instance)
      print("start register --------------------------------------------- 1")
  }

    

    public func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [AnyHashable : Any] = [:]) -> Bool {
        print("start application --------------------------------------------- 2")
        
        
        FirebaseApp.configure()

          // [START set_messaging_delegate]
          Messaging.messaging().delegate = self
          // [END set_messaging_delegate]



    //      Messaging.messaging().fcmToken { fcmToken, error in
    //        if let error = error {
    //          print("Error fetching FCM registration token: \(error)")
    //        } else if let token = token {
    //          print("FCM registration token: \(token)")
    //          self.fcmRegTokenMessage.text  = "Remote FCM registration token: \(token)"
    //        }
    //      }




          // Register for remote notifications. This shows a permission dialog on first run, to
          // show the dialog at a more appropriate time move this registration accordingly.
          // [START register_for_notifications]
          if #available(iOS 10.0, *) {
            // For iOS 10 display notification (sent via APNS)
            UNUserNotificationCenter.current().delegate = self

            let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
            UNUserNotificationCenter.current().requestAuthorization(
              options: authOptions,
              completionHandler: { _, _ in }
            )
          } else {
            let settings: UIUserNotificationSettings =
              UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
              application.registerUserNotificationSettings(settings)
          }

          application.registerForRemoteNotifications()
        

        // [END register_for_notifications]
        
        
        return true
    }
    

    

    
    
    
  public func messaging(_ messaging: Messaging, didReceive remoteMessage: Any) {

 

  }

    
  public func messaging( didReceiveRegistrationToken fcmToken: Any) {
      print("Firebase registration token: \(String(describing: fcmToken))")
//      let dataDict: [String: String] = ["token": fcmToken ?? ""]
//      NotificationCenter.default.post(
//        name: Notification.Name("FCMToken"),
//        object: nil,
//        userInfo: dataDict
//      )
      // TODO: If necessary send token to application server.
      // Note: This callback is fired at each app startup and whenever a new token is generated.
  }
   



  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
    
    
    
    
    
}


