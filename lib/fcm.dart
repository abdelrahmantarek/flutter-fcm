
import 'dart:async';

import 'package:flutter/services.dart';



class Fcm {

  static const MethodChannel _channel = MethodChannel('fcm');
  static const EventChannel _stream =  EventChannel('notification.eventchannel.sample/stream');

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

  static Stream<dynamic> get onClickNotification{
    return _stream.receiveBroadcastStream().map<dynamic>((dynamic event) => event);
  }

}
