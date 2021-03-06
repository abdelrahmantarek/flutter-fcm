
import 'dart:async';
import 'dart:io';

import 'package:fcm/android_settings.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
export 'package:fcm/android_settings.dart';



class Fcm {


  static const MethodChannel _channel = MethodChannel('fcm');
  static ValueChanged<dynamic>? onClickNotification;
  static ValueChanged<dynamic>? onMessage;



  static startListener({AndroidSettings? androidSettings}){
    _channel.setMethodCallHandler((MethodCall call) => _handleMethodCall(call));
     androidSettings ??= AndroidSettings();
    _channel.invokeMethod("startListener",{"android_settings":androidSettings.toJson()});
  }


  static _handleMethodCall(MethodCall call) async{
    // print("MethodCall : " + call.method);
    switch(call.method){
      case "on_click_notification":
        onClickNotification!(call.arguments);
        break;
        case "on_message":
        onMessage!(call.arguments);
        break;
      default:
        throw MissingPluginException();
    }
    return Future<dynamic>.value();
  }


  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String?> get getToken async {
    final String? version = await _channel.invokeMethod('getToken');
    return version;
  }

  static Future<bool> deleteToken() async {
    final bool version = await _channel.invokeMethod('deleteToken');
    await Future.delayed(const Duration(seconds: 3));
    dynamic refresh = Fcm.getToken;
    return version;
  }

  static Future<bool> pauseNotification() async {
    final bool version = await _channel.invokeMethod('pause_notification');
    return version;
  }

  static Future<bool> resumeNotification() async {
    final bool version = await _channel.invokeMethod('resume_notification');
    return version;
  }

  static Future<bool> cancelAllNotification() async {
    final bool version = await _channel.invokeMethod('cancel_all_notification');
    return version;
  }

  static Future<bool> showNotification({String title = "title",String body = "body",Map<String,dynamic>? data}) async {
    final bool version = await _channel.invokeMethod('show_notification',{"title":title,"body":body,"data":data ?? {}});
    return version;
  }



  /// Subscribe to topic in background.
  ///
  /// [topic] must match the following regular expression:
  /// `[a-zA-Z0-9-_.~%]{1,900}`.
  static Future<void> subscribeToTopic(String topic) {
    _assertTopicName(topic);
    return _channel.invokeMethod('subscribeToTopic',{"topic":topic});
  }

  /// Unsubscribe from topic in background.
  static Future<void> unsubscribeFromTopic(String topic) {
    _assertTopicName(topic);
    return _channel.invokeMethod('unsubscribeFromTopic',{"topic":topic});
  }
  static _assertTopicName(String topic) {
    bool isValidTopic = RegExp(r'^[a-zA-Z0-9-_.~%]{1,900}$').hasMatch(topic);
    assert(isValidTopic);
  }


}
