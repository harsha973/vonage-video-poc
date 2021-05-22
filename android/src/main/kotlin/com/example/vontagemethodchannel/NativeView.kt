package com.example.vontagemethodchannel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import io.flutter.plugin.platform.PlatformView

class NativeView(context: Context, id: Int, creationParams: Map<String, Any>?) : PlatformView {
    private val view: View = LayoutInflater.from(context).inflate(R.layout.layout_video, null)
    val publisherView: FrameLayout get() = view.findViewById(R.id.publisherLayout)
    val subscriberView: FrameLayout get() = view.findViewById(R.id.subsciberLayout)

    override fun getView(): View {
        return view
    }

    override fun dispose() {}
}