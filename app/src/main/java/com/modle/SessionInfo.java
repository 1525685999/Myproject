package com.modle;

/**
 * Created by user on 2016/5/6.
 */
public class SessionInfo {
    protected long sessionId;
    protected String ipAddress;



    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
