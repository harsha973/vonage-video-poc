package com.example.vontagemethodchannel

import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** VontagemethodchannelPlugin */
class VontagemethodchannelPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private val vonageListener = VonageListener()

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "vontagemethodchannel")
        channel.setMethodCallHandler(this)

        vonageListener.attachNativeView(flutterPluginBinding)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "initSession") {
            result.success(initSession(call.argument<String>("sessionId"), call.argument<String>("token"), call.argument<String>("apiKey")))
        } else if (call.method == "endSession") {
            result.success(vonageListener.endSession())
        } else if (call.method == "publishStream") {
            result.success(vonageListener.publishStream(call.argument<String>("name")))
        } else if (call.method == "unpublishStream") {
            result.success(vonageListener.unpublishStream())
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun initSession(sessionId: String?, token: String?, apiKey: String?): String {
        vonageListener.initializeSession(
                sessionId = sessionId!!,
                token = token!!,
                apiKey = apiKey!!
        )
        // requestPermissions();
        return ""
    }

}
