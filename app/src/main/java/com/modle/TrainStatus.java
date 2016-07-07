package com.modle;

/**
 * Created by user on 2016/5/9.
 */
public enum  TrainStatus {
    NOT_SET("NotSet"),
    SUCCEEDED("Succeeded"),
    FAILED("Failed");
    private final String value;

    TrainStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TrainStatus fromValue(String v) {
        for (TrainStatus c: TrainStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }


}
