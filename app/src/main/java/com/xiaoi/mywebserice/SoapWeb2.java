package com.xiaoi.mywebserice;

import android.util.Log;

import com.com.xiaoi.sample.Base64;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by user on 2016/5/6.
 */
public class SoapWeb2 {
    SoapProxy soapProxy;
    public SoapWeb2() {
       soapProxy=new SoapProxy();
    }

    public SoapObject enroll(long SessionId,String speakerID, String voiceprintTag, File file, String spokenText, String configSetName) throws IOException, XmlPullParserException {

        String base64 = Base64.encodeFromFile(file.toString());
        return soapProxy.callws("Enroll", new Object[]{"sessionId", SessionId}
                ,new Object[]{"speakerId", speakerID}
                ,new Object[]{"voiceprintTag", voiceprintTag}
                ,new Object[]{"audio", base64}
                ,new Object[]{"text", spokenText}
                ,new Object[]{"configSetName", configSetName}
        );
    }

    public SoapObject train( long SessionId,String speakerID,String voiceprintTag,String configSetName) throws IOException, XmlPullParserException {
        return soapProxy.callws("Train", new Object[]{"sessionId", SessionId}
                ,new Object[]{"speakerId", speakerID}
                ,new Object[]{"voiceprintTag", voiceprintTag}
                ,new Object[]{"configSetName", configSetName}
        );
    }

    public SoapObject endSession( long SessionId,String configSetName) throws IOException, XmlPullParserException {

        return soapProxy.callws("EndSession", new Object[]{"sessionId", SessionId}

                ,new Object[]{"configSetName", configSetName}
        );
    }

    public SoapObject startSession(String configSetName) throws IOException, XmlPullParserException {
        return soapProxy.callws("StartSession",
                new Object[]{"configSetName", configSetName},
                new Object[]{"externalSessionId",""});
    }

   public SoapObject verify(long SessionId,String speakerID, String voiceprintTag, String commonPassphraseAudio, String spokenText, String configSetName) throws IOException, XmlPullParserException {
      // sessionInfo.getSessionId(), speakerID, voiceprintTag, commonPassphraseAudio, spokenText, configSetName

       return soapProxy.callws("Verify", new Object[]{"sessionId", SessionId}
               ,new Object[]{"speakerId", speakerID}
               ,new Object[]{"voiceprintTag", voiceprintTag}
               ,new Object[]{"audio", commonPassphraseAudio}
               ,new Object[]{"text", spokenText}
               ,new Object[]{"configSetName", configSetName}
       );


   }
    public Object startAudio(long SessionId,String configSetName) throws IOException, XmlPullParserException {
        //sessionInfo.getSessionId(), configSetName

        Object object=soapProxy.calls2("StartAudio", new Object[]{"sessionId", SessionId}

                ,new Object[]{"configSetName", configSetName}
        );
        Log.i("Tree",object.toString());
        return object;
    }

    public Object addAudio(long SessionId,File file,String audioSegmentId,String configSetName) throws IOException, XmlPullParserException {
       // sessionInfo.getSessionId(), commonPassphraseAudio, base64, configSetName
        String base64 = Base64.encodeFromFile(file.toString());

        return soapProxy.calls2("AddAudio", new Object[]{"sessionId", SessionId}
                ,new Object[]{"audioSegmentId", audioSegmentId}
                ,new Object[]{"audio", base64}
                ,new Object[]{"configSetName", configSetName}
        );
    }
   public Object endAudio(long SessionId,String audioSegmentId,String configSetName) throws IOException, XmlPullParserException {
//       sessionInfo.getSessionId(), commonPassphraseAudio, configSetName
       return soapProxy.calls2("EndAudio", new Object[]{"sessionId", SessionId}
               ,new Object[]{"audioSegmentId", audioSegmentId}
               ,new Object[]{"configSetName", configSetName}
       );
   }
///////////////////////////////////////////////////////////////////////

  public Object GetSpeakersList(String configSetName,int size) throws IOException, XmlPullParserException {
      return soapProxy.calls2("GetSpeakersList",new Object[]{"scopeName",configSetName}
              ,new Object[]{"maxResults",size});
  }
//    <sessionId>long</sessionId>
//    <speakerId>string</speakerId>
//    <voiceprintTag>string</voiceprintTag>
//    <configSetName>string</configSetName>
    public Object IsTrained (long sessionId,String speakerId,String voiceprintTag,String configSetName) throws IOException, XmlPullParserException {
        return soapProxy.calls2("IsTrained",new Object[]{"sessionId",sessionId}
                ,new Object[]{"speakerId",speakerId}
                ,new Object[]{"voiceprintTag",voiceprintTag}
                ,new Object[]{"configSetName",configSetName}
        );
    }
    public Object UploadRBDInformation(String backgroundModelName,String backgroundModelVersion,File RBDInformation,String scopeName) throws IOException, XmlPullParserException {

        BufferedReader br = new BufferedReader(new FileReader(RBDInformation));//构造一个BufferedReader类来读取文件
        String s = null;
        String result = "";
        while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            result = result + "\n" +s;
        }
        br.close();
        String RBDInformations=Base64.encodeFromFile(RBDInformation.toString());
        return soapProxy.calls2("UploadRBDInformation",new Object[]{"backgroundModelName",backgroundModelName}
        ,new Object[]{"backgroundModelVersion",backgroundModelVersion}
        ,new Object[]{"RBDInformation",RBDInformations}
        ,new Object[]{"scopeName",scopeName});
    }

    public Object GetBackgroundModelsList(String scopeName,boolean onlyActive) throws IOException, XmlPullParserException {
//        <GetBackgroundModelsList xmlns="http://www.nuance.com/webservices/">
//        <scopeName>string</scopeName>
//        <onlyActive>boolean</onlyActive>
//        </GetBackgroundModelsList>
        return soapProxy.calls2("GetBackgroundModelsList",new Object[]{"scopeName",scopeName}

        ,new Object[]{"onlyActive",onlyActive}
        );
    }
}
