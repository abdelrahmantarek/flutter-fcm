import Flutter
import UIKit
import FirebaseMessaging
import FirebaseCore



public class SwiftFcmPlugin: NSObject, FlutterPlugin,MessagingDelegate {
    

  var window: UIWindow?
  let gcmMessageIDKey = "gcm.message_id"
  var channel: FlutterMethodChannel?
    
    
    
    
    
    
    
    
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "fcm", binaryMessenger: registrar.messenger())
    let instance = SwiftFcmPlugin()
    instance.channel = channel;
    registrar.addMethodCallDelegate(instance, channel: channel)
    registrar.addApplicationDelegate(instance)
      print("start register --------------------------------------------- 1")
  }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    public func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [AnyHashable : Any] = [:]) -> Bool {
        print("start application --------------------------------------------- 2")
        

          // [START set_messaging_delegate]
          Messaging.messaging().delegate = self
          // [END set_messaging_delegate]


        if let token = Messaging.messaging().fcmToken {
            print("fcmToken ---------------------------------------------\(String(describing: token)):  " )
        }

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
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    public func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        let userInfo = response.notification.request.content.userInfo
           // Print message ID.
           if let messageID = userInfo[gcmMessageIDKey] {
               print("Message ID: \(messageID)")
           }
          channel?.invokeMethod("on_click_notification", arguments: response.notification.request.content.categoryIdentifier)
          completionHandler()
    }
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        // Forground notifications.
        completionHandler([.alert, .sound])
    }

    
    
    
    
    
    
    
    
    
    
    
    
    public func application(_ application: UIApplication,didReceiveRemoteNotification userInfo: [AnyHashable: Any]) {
      // If you are receiving a notification message while your app is in the background,
      // this callback will not be fired till the user taps on the notification launching the application.
      // TODO: Handle data of notification
      // With swizzling disabled you must let Messaging know about the message, for Analytics
       Messaging.messaging().appDidReceiveMessage(userInfo)
      // Print message ID.
      if let messageID = userInfo[gcmMessageIDKey] {
        print("Message ID: \(messageID)")
      }

//      print(userInfo)
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    //    // [START receive_message]
    public func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) -> Bool {
              // If you are receiving a notification message while your app is in the background,
              // this callback will not be fired till the user taps on the notification launching the application.
              // TODO: Handle data of notification
              // With swizzling disabled you must let Messaging know about the message, for Analytics
               Messaging.messaging().appDidReceiveMessage(userInfo)
              // Print message ID.
              if let messageID = userInfo[gcmMessageIDKey] {
                print("Message UIBackgroundFetchResult ID: \(messageID)")
              }
        
              send_Noti(userInfo: userInfo as NSDictionary)
        
              completionHandler(UIBackgroundFetchResult.newData)

        return true
    }
    // [END receive_message]
    
 
    
    
    
    
    
    
    
    
    
    
    
    

    
    
    
    func send_Noti(userInfo:NSDictionary?){
            
             let data = try! JSONSerialization.data(withJSONObject: userInfo, options: .prettyPrinted)
          

            let content = UNMutableNotificationContent()
//            let requestIdentifier = "rajanNotification"

            content.badge = 1
            content.title = userInfo!["title"] as? String ?? String("w");
//          content.subtitle = userInfo!["body"] as? String ?? String("w");
            content.body = userInfo!["body"] as? String ?? String("w");
            content.categoryIdentifier = String(data: data, encoding: .utf8)!
            content.sound = UNNotificationSound.default

        
            let request = UNNotificationRequest(identifier: String(data: data, encoding: .utf8)!, content: content, trigger: nil)
            UNUserNotificationCenter.current().add(request) { (error:Error?) in
                if error != nil {
                    print(error?.localizedDescription ?? "some unknown error")
                }
            }
        }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public func application(_ application: UIApplication,didFailToRegisterForRemoteNotificationsWithError error: Error) {
      print("Unable to register for remote notifications: \(error.localizedDescription)")
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    // This function is added here only for debugging purposes, and can be removed if swizzling is enabled.
    // If swizzling is disabled then this function must be implemented so that the APNs token can be paired to
    // the FCM registration token.
    public func application(_ application: UIApplication,didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
      print("APNs token retrieved: \(deviceToken)")
      // With swizzling disabled you must set the APNs token here.
       Messaging.messaging().apnsToken = deviceToken
    }
    

    

    
    
    
 

    

    
    
    
    
    


  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }

    
    
    
    
    
    
    
    
    
    func messaging(_ messaging: Messaging, fcmToken: String?) {
        print("fcmToken ---------------------------------------------:   \(String(describing: fcmToken))")
        print("Firebase registration token: \(String(describing: fcmToken))")
        let dataDict: [String: String] = ["token": fcmToken ?? ""]
        NotificationCenter.default.post(
          name: Notification.Name("FCMToken"),
          object: nil,
          userInfo: dataDict
        )
        // TODO: If necessary send token to application server.
        // Note: This callback is fired at each app startup and whenever a new token is generated.
      }
}


extension String {
    func utf8DecodedString()-> String {
        let data = self.data(using: .utf8)
        let message = String(data: data!, encoding: .nonLossyASCII) ?? ""
        return message
    }
    
    func utf8EncodedString()-> String {
        let messageData = self.data(using: .nonLossyASCII)
        let text = String(data: messageData!, encoding: .utf8) ?? ""
        return text
    }
}
