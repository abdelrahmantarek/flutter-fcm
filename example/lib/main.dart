import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:fcm/fcm.dart';


//
// abstract class ClickNotificationListener {
//   abstract fun onClickNotification(intent: Intent?);
// }
//
// class BroadCastClickNotification : BroadcastReceiver() {
//
//   private lateinit var callback: ClickNotificationListener
//
//   fun setListener(callback: ClickNotificationListener) {
//   this.callback = callback;
//   }
//
//   override fun onReceive(context: Context?, intent: Intent?) {
//   callback.onClickNotification(intent)
//   Log.d("event_listener","new notification");
//   }
// }


void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {


  String _platformVersion = 'Unknown';
  StreamSubscription? streamSubscription;


  @override
  void dispose() {
    streamSubscription!.cancel();
    super.dispose();
  }


  @override
  void initState(){
    super.initState();
    Fcm.getToken.then((value){
      print(value);
    });
    streamSubscription = Fcm.onClickNotification.listen((event) {
      print(event.toString());
      setState(() {
        _platformVersion = event;
      });
    });
  }


  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await Fcm.platformVersion ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }
  // git remote add my_awesome_new_remote_repo https://github.com/abdelrahmantarek/flutter-fcm.git

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }


}
