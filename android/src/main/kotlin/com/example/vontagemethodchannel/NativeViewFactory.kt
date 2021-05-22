package com.example.vontagemethodchannel

import android.content.Context
import android.widget.FrameLayout
import com.example.vontagemethodchannel.NativeView
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformViewFactory

class NativeViewFactory : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
   private lateinit var platformView: NativeView
    override fun create(context: Context, id: Int, args: Any?): NativeView {
        val creationParams = args as Map<String, Any>?
        platformView = NativeView(context, id, creationParams)
        return platformView
    }

    val publisherView: FrameLayout
        get() = platformView.publisherView

    val subscriberView: FrameLayout
        get() = platformView.subscriberView
}