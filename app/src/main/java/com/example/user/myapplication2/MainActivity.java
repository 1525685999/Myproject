package com.example.user.myapplication2;

import com.Style.PhoneHead;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.modle.Result;
import com.modle.SessionInfo;
import com.storage.Constact;
import com.storage.UserSharedPreferences;
import com.storage.VoiceprintTagSharedPreferences;
import com.xiaoi.mywebserice.SoapClinet;
import com.xiaoi.mywebserice.SoapWeb;
import com.xiaoi.mywebserice.SoapWeb2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity implements OnClickListener {
    private Button loginbnt;
    private TextView registertxt;
    private EditText userName, password;
    UserSharedPreferences userSharedPreferences;
    private SoapClinet soapClinet;
    private Button sy;
    private ProgressDialog pd;
    String str;
    Handler hd;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    VoiceprintTagSharedPreferences voiceprintTagSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
       // PhoneHead.HeadStyle(this);
        setContentView(R.layout.activity_main);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        loginbnt = (Button) this.findViewById(R.id.loginbnt);
        registertxt = (TextView) this.findViewById(R.id.registertxt);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        registertxt = (TextView) findViewById(R.id.registertxt);
        loginbnt.setOnClickListener(this);
        registertxt.setOnClickListener(this);
        userSharedPreferences = new UserSharedPreferences(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        voiceprintTagSharedPreferences=new VoiceprintTagSharedPreferences(this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        sy= (Button) this.findViewById(R.id.sy);
        sy.setOnClickListener(this);
        soapClinet = new SoapClinet();
//        InputStream inputStream  = getResources().openRawResource(R.raw.rbdXiaoiMobile) ;
//        InputStreamReader inputStreamReader = null;
//        try {
//            inputStreamReader = new InputStreamReader(inputStream , "UTF-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String info = "";
//            while ((info = bufferedReader.readLine()) != null) {
//                Log.i("info", info);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
      userName.setText(userSharedPreferences.getspeakerID());
        password.setText(userSharedPreferences.getPassword());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId()) {
            case R.id.loginbnt:
                String uNamne = userName.getText().toString();
                String uPassword = password.getText().toString();

                if (uNamne != null && uPassword != null && !uNamne.equals("") && !uPassword.equals("")) {
                    userSharedPreferences.putUserName(uNamne);
                    userSharedPreferences.putPassword(uPassword);

                    if (!userSharedPreferences.getUserName(uNamne).equals(uNamne)) {
                        userName.setError("用户名错误");
                    } else if (false) {
                        password.setError("密码错误");
                    } else if (userSharedPreferences.getUserName(uNamne).equals(uNamne)) {
//                       String[] st=voiceprintTagSharedPreferences.getVoiceprintTagResult(uNamne).split("_");
//                        if (st.length>0 && st[0].equals(uNamne)){
//                            if(Boolean.parseBoolean(st[1])){
//                                LoginDialog(true);
//                            }else{
//                                LoginDialog(false);
//                            }
//                        }else{
//                            LoginDialog(false);
//                        }
                        userSharedPreferences.putspeakerID(uNamne);
                      // Log.i("mine",userSharedPreferences.getspeakerID()+"666");
                        pd = ProgressDialog.show(this, null, "正在验证，请稍等");
                        new IsTrainedAsynctask().execute();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "用户名或者密码为空", Toast.LENGTH_SHORT).show();
                }


//			 new MyThread().start();
                break;
            case R.id.registertxt:
                intent = new Intent(this, RegisterActivty.class);
                startActivity(intent);
                break;
            case R.id.sy:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        SoapWeb2 soapWeb2=new SoapWeb2();
//                        try {
//                            //soapWeb2.UploadRBDInformation("624-bgmodel","10.0.2.0",new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"rbdXiaoiMobile.rbd"),"Test01_HW_20160413");
//                            str=soapWeb2.GetBackgroundModelsList("Test01_HW_20160413",false).toString();
//                            Message message=new Message();
//                            message.what=0;
//                            hd.sendMessage(message);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (XmlPullParserException e) {
//                            e.printStackTrace();
//                        }
                   }
                }).start();

                break;
            default:
                break;
        }

    }

    /**
     *  已经注册过为true，没有注册过为false
     */
    public void LoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);  //先得到构造器
//        String text;
//        String bnttext2 = "主页";
//        String bnttext1= "主页";
//        if (TureOrFalse) {
//            text = "你是已经注册过声纹的用户了,请选择进入主页面，还是放弃原来的声纹库，重新注册";
//            bnttext1="重新注册";
//        } else {
//            text = "你没有注册过声纹，请去注册";
//            bnttext2 = "取消";
//            bnttext1="注册";
//
//        }
        builder.setMessage("您没有注册过声纹，请去注册"); //设置内容
        builder.setTitle("提示"); //设置标题
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("注册", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog

                Intent intent2 = new Intent(MainActivity.this, VoiceintruvtionActivty.class);
                startActivity(intent2);

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
//                if (TureOrFalse) {
//                    Intent intent1 = new Intent(MainActivity.this, AccountrActivty.class);
//                    startActivity(intent1);
//                } else {
//                    dialog.dismiss();
//                }
            }
        });
        builder.create().show();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

   private class IsTrainedAsynctask extends AsyncTask<Void, Void, String>{


       @Override
       protected String doInBackground(Void... params) {

           return isTrain(userSharedPreferences.getspeakerID(),userSharedPreferences.getspeakerID(),Constact.configSetName);
       }

       @Override
       protected void onPostExecute(String aBoolean) {
           super.onPostExecute(aBoolean);
           pd.dismiss();
           if (aBoolean==null || aBoolean.equals("")){
               Toast.makeText(MainActivity.this,"连接服务器失败，请检查网络是否正确",Toast.LENGTH_SHORT).show();
           }else{
               if (aBoolean.equals("OK")){
                   Intent intent=new Intent(MainActivity.this,AccountrActivty.class);
                   startActivity(intent);
               }else if (aBoolean.equals("NO")){
                   LoginDialog();
               }
           }

       }
   }

    public String isTrain(String speakerId,String voiceprintTag,String configSetNam){

        String result =null;
        SessionInfo sessionInfo=soapClinet.startVBSession(configSetNam);
        try {
           boolean trueORfalse=soapClinet.IsVBTrained(sessionInfo.getSessionId(),speakerId,voiceprintTag,configSetNam);
            if (trueORfalse){
                result="OK";
            }else{
                result="NO";
            }
        } catch (IOException e) {
            e.printStackTrace();
            result=null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }finally {
            soapClinet.endVBSession(sessionInfo,configSetNam);
        }
        return result;
    }


}
