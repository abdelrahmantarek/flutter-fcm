#import "FcmPlugin.h"
#if __has_include(<fcm/fcm-Swift.h>)
#import <fcm/fcm-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "fcm-Swift.h"
#endif

@implementation FcmPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFcmPlugin registerWithRegistrar:registrar];
}
@end
