package com.example.vontagemethodchannel

import android.content.Context
import android.util.Log
import com.opentok.android.*
import io.flutter.embedding.engine.plugins.FlutterPlugin

internal class VonageListener : Session.SessionListener, PublisherKit.PublisherListener {
    private val LOG_TAG = "VonageVideoPlugin"
    private var mSession: Session? = null
    private var mPublisher: Publisher? = null
    private var subscriber: Subscriber? = null
    private var nativeView = NativeViewFactory()
    private lateinit var context: Context

    fun attachNativeView(binding: FlutterPlugin.FlutterPluginBinding) {
        context = binding.applicationContext
        binding.platformViewRegistry.registerViewFactory("flutter-vonage-video-publisher", nativeView)
    }

    // SessionListener methods
    override fun onConnected(session: Session) {
        Log.i(LOG_TAG, "Session Connected")
    }

    override fun onDisconnected(session: Session) {
        Log.i(LOG_TAG, "Session Disconnected")
    }

    override fun onStreamReceived(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Received")
        if (subscriber == null) {
            subscriber = Subscriber.Builder(context, stream).build()
            subscriber?.renderer?.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL)
//            subscriber?.setSubscriberListener(this)
            session.subscribe(subscriber)
            nativeView.subscriberView.addView(subscriber!!.view)
        }
    }

    override fun onStreamDropped(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Dropped")

        if (subscriber != null) {
            subscriber = null
            nativeView.subscriberView.removeAllViews()
        }
    }

    override fun onError(session: Session, opentokError: OpentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.message)
    }

    // PublisherListener methods
    override fun onStreamCreated(publisherKit: PublisherKit, stream: Stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated")
    }

    override fun onStreamDestroyed(publisherKit: PublisherKit, stream: Stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed")
    }

    override fun onError(publisherKit: PublisherKit, opentokError: OpentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.message)
    }

    fun initializeSession(sessionId: String, token: String, apiKey: String) {
        mSession = Session.Builder(context, apiKey, sessionId).build()
        mSession?.setSessionListener(this)
        mSession?.connect(token)
    }

    fun publishStream(name: String?): String {
        mPublisher = Publisher.Builder(context).name(name).build()
        mPublisher?.setPublisherListener(this)

        // nativeView.getView().setText("publish stream");
        nativeView.publisherView.addView(mPublisher?.view)
        mSession?.publish(mPublisher)
        return ""
    }

    fun unpublishStream(): String {
        mSession!!.unpublish(mPublisher)
        nativeView.publisherView.removeAllViews()
        return ""
    }

    fun endSession(): String {
        mSession!!.disconnect()
        return ""
    }

}