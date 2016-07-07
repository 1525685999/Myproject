package com.modle;

/**
 * Created by user on 2016/5/9.
 */
public enum  DecisionReason {

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
    TONE_DETECTED("ToneDetected"),
    WRONG_PASSPHRASE("WrongPassphrase"),
    FRAUDSTER_INDICATION("FraudsterIndication"),
    VOICEPRINT_MATCH("VoiceprintMatch"),
    VOICEPRINT_MISMATCH("VoiceprintMismatch"),
    VOICEPRINT_INCONCLUSIVE("VoiceprintInconclusive"),
    UNRELIABLE_DECISION_THRESHOLDS("UnreliableDecisionThresholds");
    private final String value;

    DecisionReason(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DecisionReason fromValue(String v) {
        for (DecisionReason c: DecisionReason.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }



}
