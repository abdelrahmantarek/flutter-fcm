



class AndroidSettings{

   String icon;
   String resource;



  AndroidSettings({this.icon = "ic_launcher", this.resource = "mipmap"});


  Map<String,dynamic> toJson(){
    return {
      "icon":icon,
      "resource":resource,
    };
  }

}