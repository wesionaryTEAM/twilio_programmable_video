package twilio.flutter.twilio_programmable_video

import android.content.Context
import android.content.Intent
import com.twilio.video.LocalVideoTrack
import com.twilio.video.ScreenCapturer
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

fun screenCallback(
    requestCode: Int,
    resultCode: Int,
    data: Intent,
    applicationContext: Context,
    call: MethodCall,
    result: MethodChannel.Result
) {
    val screenCapturer = ScreenCapturer(applicationContext, resultCode, data, screenCapturerListener)

    TwilioProgrammableVideoPlugin.screenCapturer = screenCapturer as ScreenCapturer
    val screenTrack = LocalVideoTrack.create(applicationContext, true,
            TwilioProgrammableVideoPlugin.screenCapturer!!, "ScreenCapture")
    if (screenTrack != null) {
        val localParticipant = TwilioProgrammableVideoPlugin.roomListener.room?.localParticipant
        if (localParticipant?.localVideoTracks != null)
            localParticipant.publishTrack(screenTrack)
        try {
            return result.success(true)
        } catch (e: Exception) {
            try {
                return result.error("Screen capture", e.message, null)
            } catch (e: Exception) {
            }
        }
    }
}

val screenCapturerListener: ScreenCapturer.Listener = object : ScreenCapturer.Listener {
    override fun onScreenCaptureError(errorDescription: String) {
    }

    override fun onFirstFrameAvailable() {
        TwilioProgrammableVideoPlugin.handler.post {
            TwilioProgrammableVideoPlugin.debug("CameraCapturer.onFirstFrameAvailable")
            TwilioProgrammableVideoPlugin.pluginHandler.sendCameraEvent("firstFrameAvailable", mapOf("capturer" to VideoCapturerHandler.videoCapturerToMap(TwilioProgrammableVideoPlugin.cameraCapturer!!)), null)
        }
    }
}