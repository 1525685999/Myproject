package com.xiaoi.mywebserice;

import com.modle.SessionInfo;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by user on 2016/6/20.
 */
public class SoapClinet extends SoapWeb2 {
    private SoapWeb2 soapweb;

    public SoapClinet() {
        this.soapweb =new SoapWeb2();
    }

    public SessionInfo startVBSession(String configSetName) {
        SessionInfo sessionInfo = new SessionInfo();
        try {
            SoapObject sessionInfoSoapObject = soapweb.startSession(configSetName);
            sessionInfo.setSessionId(Long.parseLong(sessionInfoSoapObject.getProperty("SessionId").toString()));
            sessionInfo.setIpAddress(sessionInfoSoapObject.getProperty("IPAddress").toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return sessionInfo;
    }

    public void endVBSession(SessionInfo sessionInfo,String configSetName) {
        // Ensure that even if there's an exception, the session will closed properly
        if (sessionInfo != null) {
//                <sessionId>long</sessionId>
//                <configSetName>string</configSetName>
            try {
                soapweb.endSession(sessionInfo.getSessionId(), configSetName);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean IsVBTrained(long sessionId,String speakerId,String voiceprintTag,String configSetName) throws IOException, XmlPullParserException {
        return Boolean.parseBoolean(soapweb.IsTrained(sessionId,speakerId,voiceprintTag,configSetName).toString());
    }
}
