package com.modle;

/**
 * Created by user on 2016/5/9.
 */
public enum InvalidityReason {

    NOT_SET("NotSet"),
    INTERNAL_ERROR("InternalError"),
    AUDIO_OK("AudioOK"),
    NOT_ENOUGH_AUDIO("NotEnoughAudio"),
    INVALID_AUDIO("InvalidAudio"),
    AUDIO_TOO_SHORT("AudioTooShort"),
    AUDIO_TOO_SOFT("AudioTooSoft"),
    AUDIO_TOO_LOUD("AudioTooLoud"),
    AUDIO_TOO_NOISY("AudioTooNoisy"),
    MULTI_SPEAKERS_DETECTED("MultiSpeakersDetected"),
    SYNTHETIC_SPEECH_DETECTED("SyntheticSpeechDetected"),
    PLAYBACK_INDICATION("PlaybackIndication"),
    WRONG_PASSPHRASE("WrongPassphrase"),
    FRAUDSTER_INDICATION("FraudsterIndication");
    private final String value;

    InvalidityReason(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InvalidityReason fromValue(String v) {
        for (InvalidityReason c: InvalidityReason.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }


}
