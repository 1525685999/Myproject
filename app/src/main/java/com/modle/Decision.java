package com.modle;

/**
 * Created by user on 2016/5/9.
 */
public enum  Decision {

    NOT_SET("NotSet"),
    MISMATCH("Mismatch"),
    INCONCLUSIVE("Inconclusive"),
    MATCH("Match"),
    FAILURE("Failure"),
    UNRELIABLE("Unreliable");
    private final String value;

    Decision(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Decision fromValue(String v) {
        for (Decision c: Decision.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }



}
