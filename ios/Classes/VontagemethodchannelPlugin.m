#import "VontagemethodchannelPlugin.h"
#if __has_include(<vontagemethodchannel/vontagemethodchannel-Swift.h>)
#import <vontagemethodchannel/vontagemethodchannel-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "vontagemethodchannel-Swift.h"
#endif

@implementation VontagemethodchannelPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftVontagemethodchannelPlugin registerWithRegistrar:registrar];
}
@end
