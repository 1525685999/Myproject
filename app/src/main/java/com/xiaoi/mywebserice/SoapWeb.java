package com.xiaoi.mywebserice;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.NtlmTransport;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.util.Log.*;


public class SoapWeb {

//	// WSDL文档中的命名空间
//			String targetNameSpace = "http://WebXml.com.cn/";
//			// WSDL文档中的URL
//			String WSDL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx?wsdl";
//			// 需要调用的方法名(获得本天气预报Web Services支持的洲、国内外省份和城市信息)
//			String getSupportProvince = "getSupportProvince";


    String targetNameSpace = "http://www.nuance.com/webservices/";
    String getSupportProvince = "StartSession";
    String WSDL = "http://172.16.5.11/SecuritySuite/VocalPasswordServer.asmx";// 后面加不加那个?wsdl参数影响都不大
    String username = "administrator";
    String password = "Sds2sdf21rt";


    // String speakerID = "Speaker2";
    // String configSetName = "config1";
    // String spokenText = "My voice is my password"; // Note: this is optional
    // in
    // // Text Dependent but
    // // mandatory in Text
    // // Prompted.
    // String voiceprintTag = "MyVoiceprint2";

//	String endPoint = "http://" + "172.16.5.11" + "/SecuritySuite/VocalPasswordServer.asmx";// 请求地址
//	String targetNamespace = "http://tempuri.org/";// 命名空间这个值随便设置
//	String method = "startSession";//需要调用的方法 wsdl里面看
//	String soapAction = "http://tempuri.org/" + method;//他等于空间名加上
//
//	public String linkWeb() {
//		HttpTransportSE httpTransportSE = new HttpTransportSE(endPoint);
//		SoapObject rpc = new SoapObject(targetNamespace, method);
//		// String username = "administrator";
//		// String password = "Sds2sdf21rt";
//		rpc.addProperty("configSetName", "config1");
//		rpc.addProperty("externalSessionId", "");
//
//		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
//		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
//
//		envelope.bodyOut = rpc;
//		// 设置是否调用的是dotNet开发的WebService
//		envelope.dotNet = true;
//		// 等价于envelope.bodyOut = rpc;
//		envelope.setOutputSoapObject(rpc);
//
//		String errMsg = null;
//		try {
//			httpTransportSE.call(soapAction, envelope);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (XmlPullParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			errMsg = e.getMessage();
//		}
//
//		// 获取返回的数据
//		SoapObject object = (SoapObject) envelope.bodyIn;
//		// 获取返回的结果
//		if (object != null) {
//			return object.getProperty(0).toString();
//		} else {
//			return errMsg;
//		}
//
//	}


    public String linkWeb2() {

        List<String> provinces = new ArrayList<String>();

        SoapObject soapObject = new SoapObject(targetNameSpace, getSupportProvince);
        soapObject.addProperty("configSetName", "config1");
        soapObject.addProperty("externalSessionId", "");


        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本（SoapEnvelope.VER11这就是他的版本）
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);//envelope.bodyOut=request;


        NtlmTransport httpTranstation = new NtlmTransport(WSDL);
        httpTranstation.setCredentials("administrator", "Sds2sdf21rt", "", "");//todo
        httpTranstation.debug = true;
        //或者HttpTransportSE httpTranstation=new HttpTransportSE(WSDL);
        try {

            httpTranstation.call(targetNameSpace + getSupportProvince, envelope);
            SoapObject result = (SoapObject) envelope.bodyIn;
            //下面对结果进行解析，结构类似json对象
            //str=(String) result.getProperty(6).toString();

            int count = result.getPropertyCount();//得到数据的条数
            for (int index = 0; index < count; index++) {
                provinces.add(result.getProperty(index).toString());
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.printf(httpTranstation.responseDump);
        }
        return null;
    }

//	public String link3(){
//		String responseBody =null;
//	     DefaultHttpClient httpclient = new DefaultHttpClient();
//         // register ntlm auth scheme
//         httpclient.getAuthSchemes().register("ntlm", new NTLMSchemeFactory());
//         httpclient.getCredentialsProvider().setCredentials(
//                 // Limit the credentials only to the specified domain and port
//                 new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
//                 (Credentials) // Specify credentials, most of the time only user/pass is needed
//                 new NTCredentials(username, password, "", "")
//         );
//
//         HttpUriRequest httpget = new HttpGet("http://" + "172.16.5.11" + "/SecuritySuite/VocalPasswordServer.asmx");
//         HttpResponse response = null;
//		try {
//			response = httpclient.execute(httpget);
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//         try {
//			 responseBody = EntityUtils.toString(response.getEntity());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//         return responseBody;
//	}

    public String getRemoteInfo() {
        String nameSpace = "http://www.nuance.com/webservices/";
        String methodName = "StartSession";
        String soapAction = "http://www.nuance.com/webservices/" + methodName;
        String endPoint = "http://172.16.5.11/SecuritySuite/VocalPasswordServer.asmx";// 后面加不加那个?wsdl参数影响都不大

        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = new SoapObject(nameSpace, methodName);

        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        rpc.addProperty("configSetName", "config1");
        rpc.addProperty("externalSessionId", "");

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);


        NtlmTransport transport = new NtlmTransport(endPoint);
        transport.setCredentials("administrator", "Sds2sdf21rt", "", "");
        transport.debug = true;

        try {
            // 调用WebService
            transport.call(soapAction, envelope);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf(transport.responseDump);
        }

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        if (object != null) {
            Log.i("TAG", object.getProperty(0).toString());
            return object.getProperty(0).toString();

        } else {
            return null;
        }

    }
}
