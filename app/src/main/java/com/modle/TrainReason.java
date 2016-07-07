package com.modle;

/**
 * Created by user on 2016/5/9.
 */
public enum  TrainReason {

    NOT_SET("NotSet"),
    INTERNAL_ERROR("InternalError"),
    AUDIO_OK("AudioOK"),
    NOT_ENOUGH_AUDIO("NotEnoughAudio"),
    INCONSISTENCY("Inconsistency"),
    NOT_READY_TO_TRAIN("NotReadyToTrain");
    private final String value;

    TrainReason(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TrainReason fromValue(String v) {
        for (TrainReason c: TrainReason.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }



}
