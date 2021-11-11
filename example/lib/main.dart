import 'dart:convert';

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


  Map<String,dynamic> json = {};
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
        json = jsonDecode(event);
      });
    });
  }


  /*
       return Column(children: controller.listDateTime.map((dateTime){
                      return ItemDayEditMeals(
                        dateTime: dateTime,
                      );
                    }).toList(),);
   */

  // git remote add my_awesome_new_remote_repo https://github.com/abdelrahmantarek/flutter-fcm.git

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: SingleChildScrollView(
            child: Column(
              children: [

                Column(
                  children: json.values.map((e) => Text(e)).toList(),
                ),

                SizedBox(height: 10,),
                RaisedButton(onPressed: (){
                  Fcm.pauseNotification();
                },child: Text("pause"),),

                SizedBox(height: 10,),

                RaisedButton(onPressed: (){
                  Fcm.resumeNotification();
                },child: Text("resume"),),

                SizedBox(height: 10,),

                RaisedButton(onPressed: (){
                  Fcm.deleteToken();
                },child: Text("deleteToken"),)


              ],
            ),
          ),
        ),
      ),
    );
  }


}
