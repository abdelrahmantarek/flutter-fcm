
import 'dart:async';

import 'package:flutter/services.dart';

class Fcm {

  static const MethodChannel _channel = MethodChannel('fcm');
  static const EventChannel _stream =  EventChannel('notification.eventchannel.sample/stream');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Stream<dynamic> get onClickNotification{
    return _stream.receiveBroadcastStream().map<dynamic>((dynamic event) => event);
  }

}
