package twilio.flutter.twilio_programmable_video

class AudioSettings(speakerEnabled: Boolean = true) {
    var speakerEnabled: Boolean


    init {
        this.speakerEnabled = speakerEnabled
        // this.bluetoothPreferred = bluetoothPreferred
    }

    fun reset() {
        speakerEnabled = true
        // bluetoothPreferred = false
    }
}
