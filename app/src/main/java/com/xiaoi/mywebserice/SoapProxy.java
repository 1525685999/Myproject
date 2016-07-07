package com.xiaoi.mywebserice;

import android.util.Log;

import com.com.xiaoi.sample.Base64;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.NtlmTransport;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class SoapProxy {
    String endPointHost = "http://116.228.66.178:8021";
    String username = "administrator";
    String password = "Qpmz3854";

    private String nameSpace = "http://www.nuance.com/webservices/";
    //String methodName = "StartSession";
    private String endPoint = endPointHost + "/SecuritySuite/VocalPasswordServer.asmx";// 后面加不加那个?wsdl参数影响都不大

    private NtlmTransport transport;
    private SoapSerializationEnvelope envelope;
    private long SessionId;

    public SoapProxy() {
        init();
    }

    private void init() {
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        transport = new NtlmTransport(endPoint);
        transport.setCredentials(username, password, "", "");
        
        transport.debug = true;

    }


    public SoapObject callws(String methodName, Object[]... strings) throws IOException, XmlPullParserException {
        SoapObject rpc = new SoapObject(nameSpace, methodName);
        for (Object[] string : strings) {
            rpc.addProperty(string[0].toString(), string[1]);
        }

        envelope.bodyOut = rpc;
        String soapAction = "http://www.nuance.com/webservices/" + methodName;
        transport.call(soapAction, envelope);
        SoapObject soapObject= (SoapObject) envelope.getResponse();
        return soapObject;
    }
    public Object calls2(String methodName, Object[]... strings) throws IOException, XmlPullParserException {
        SoapObject rpc = new SoapObject(nameSpace, methodName);
        for (Object[] string : strings) {
            rpc.addProperty(string[0].toString(), string[1]);
        }

        envelope.bodyOut = rpc;
        String soapAction = "http://www.nuance.com/webservices/" + methodName;
        transport.call(soapAction, envelope);
        Object soapObject=envelope.getResponse();
        return soapObject;
    }

//    SoapObject ret = soapProxy.call("StartSession", new String[]{"configSetName", "config1"}, new String[]{"externalSessionId", ""});
//    //ret.getProperty("xxx");


    public SoapObject enroll(String  methodName, String speakerID, String voiceprintTag, File file,String spokenText,String configSetName,String SessionId) throws IOException, XmlPullParserException {
//    rpc.addProperty("sessionId",SessionId);
//    rpc.addProperty("speakerId",speakerID);
//    rpc.addProperty("voiceprintTag",voiceprintTag);
    String base64 = Base64.encodeFromFile(file.toString());
//    rpc.addProperty("audio",base64);
//    rpc.addProperty("text",spokenText);
//    rpc.addProperty("configSetName",configSetName);
        return callws("enroll", new String[]{"sessionId", SessionId}
        ,new String[]{"speakerId", SessionId}
                ,new String[]{"voiceprintTag", voiceprintTag}
                ,new String[]{"audio", base64}
                ,new String[]{"text", spokenText}
                ,new String[]{"configSetName", configSetName}
        );


    }
}