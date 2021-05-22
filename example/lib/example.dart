import 'dart:io' as IO;

import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';
import 'package:vontagemethodchannel/flutter_vonage_video.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();

  @override
  StatefulElement createElement() {
    // TODO: implement createElement
    return super.createElement();
  }
}

class _MyAppState extends State<MyApp> {
  String _tokboxSessionId = '1_MX40NzIzNDM0NH5-MTYyMTY3MTEwNDU3MX53U3ZtRG9BUnF6UmZwVEI3d3VZWFN3OFp-fg';
  String _tokboxToken = 'T1==cGFydG5lcl9pZD00NzIzNDM0NCZzaWc9NTYwZDQyZWM3YzcyMzk0ZWU0ZmY1MDVkOTQ0MDYzMmZiZDVmZDdkNjpzZXNzaW9uX2lkPTFfTVg0ME56SXpORE0wTkg1LU1UWXlNVFkzTVRFd05EVTNNWDUzVTNadFJHOUJVbkY2VW1ad1ZFSTNkM1ZaV0ZOM09GcC1mZyZjcmVhdGVfdGltZT0xNjIxNjcxMTA1Jm5vbmNlPTAuODQ3OTUwMzY4MTUwNDI5NyZyb2xlPW1vZGVyYXRvciZleHBpcmVfdGltZT0xNjIxNjcyOTA1JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9';
  String _tokboxApiKey = '47234344';
  String _publishId = 'PUBLISH_ID';

  bool _sessionInited = false;
  bool _isPublishing = false;

  int _pluginViewId = -1;

  @override
  void initState() {
    super.initState();
    _initSession();
  }

  Future<void> _initSession() async {
    String ret = await FlutterVonageVideo.initSession(_tokboxSessionId, _tokboxToken, _tokboxApiKey);
    setState(() {
      _sessionInited = true;
      _isPublishing = false;
    });
  }

  Future<void> _publishStream() async {
    String ret = await FlutterVonageVideo.publishStream(_publishId, _pluginViewId);
    setState(() {
      _isPublishing = true;
    });
  }

  Future<void> _unpublishStream() async {
    String ret = await FlutterVonageVideo.unpublishStream();
    setState(() {
      _isPublishing = false;
    });
  }

  Widget? _buildPublisher(var context) {
    String viewType = 'flutter-vonage-video-publisher';
    Map<String, dynamic> creationParams = <String, dynamic> {};
    if (IO.Platform.isAndroid) {
      return PlatformViewLink(
        viewType: viewType,
        surfaceFactory: (BuildContext context, PlatformViewController controller) {
          return PlatformViewSurface(
            controller: controller,
            gestureRecognizers: const <Factory<OneSequenceGestureRecognizer>>{},
            hitTestBehavior: PlatformViewHitTestBehavior.opaque,
          );
        },
        onCreatePlatformView: (PlatformViewCreationParams params) {
          return PlatformViewsService.initSurfaceAndroidView(
            id: params.id,
            viewType: viewType,
            layoutDirection: TextDirection.ltr,
            creationParams: creationParams,
            creationParamsCodec: StandardMessageCodec(),
          )
            ..addOnPlatformViewCreatedListener(params.onPlatformViewCreated)
            ..create();
        },
      );
    } else if(IO.Platform.isIOS) {
      return UiKitView(
        viewType: viewType,
        layoutDirection: TextDirection.ltr,
        creationParams: creationParams,
        creationParamsCodec: const StandardMessageCodec(),
        onPlatformViewCreated: (int id) {
          _pluginViewId = id;
        },
      );
    }
  }

  @override
  void dispose() {
    if (_isPublishing) {
      _unpublishStream();
    }
    if (_sessionInited) {
      FlutterVonageVideo.endSession();
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    Widget _buttonPublish = SizedBox.shrink();
    if (_sessionInited && !_isPublishing) {
      _buttonPublish = ElevatedButton(
        child: Text('Publish Stream'),
        onPressed: _publishStream,
      );
    }

    Widget _buttonUnpublish = SizedBox.shrink();
    if (_sessionInited && _isPublishing) {
      _buttonUnpublish = ElevatedButton(
        child: Text('Unpublish Stream'),
        onPressed: _unpublishStream,
      );
    }

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Vonage Video example app'),
        ),
        body: Column(
            children: <Widget> [
              _buttonPublish,
              _buttonUnpublish,
              Expanded(child: Container(
                child: _buildPublisher(context),
              ),)

            ]
        ),
      ),
    );
  }
}
