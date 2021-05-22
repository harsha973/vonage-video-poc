
import 'dart:async';

import 'package:flutter/services.dart';

class Vontagemethodchannel {
  static const MethodChannel _channel =
      const MethodChannel('vontagemethodchannel');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
