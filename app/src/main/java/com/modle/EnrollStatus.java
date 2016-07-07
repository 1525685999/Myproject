package com.modle;

/**
 * Created by user on 2016/5/9.
 */
public enum EnrollStatus {
    MORE_AUDIO_REQUIRED("MoreAudioRequired"),
    READY_FOR_TRAINING("ReadyForTraining"),
    FULL("Full");
    private final String value;

    EnrollStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnrollStatus fromValue(String v) {
        for (EnrollStatus c: EnrollStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }


}
