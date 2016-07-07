package com.example.user.myapplication2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Style.PhoneHead;
import com.com.xiaoi.serve.AudioFileFunc;
import com.com.xiaoi.serve.AudioRecordFunc;
import com.com.xiaoi.serve.ErrorCode;
import com.com.xiaoi.serve.VoiceSerice;
import com.modle.History;
import com.modle.Result;
import com.modle.SessionInfo;
import com.storage.Constact;
import com.storage.UserSharedPreferences;
import com.storage.VoiceprintTagSharedPreferences;
import com.xiaoi.mywebserice.SoapWeb2;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class AuthenticationActivty extends Activity implements OnClickListener {
    private ImageView microphone;
    private AlertDialog create;
    private AlertDialog.Builder biuBuilder;//弹框
    private LayoutInflater inflater;
    private Button startORstop, cancle,listenbnt,push;
    View MicDialogview;
    private TextView esc_text;
    File audiofile;
    private UIHandler uiHandler;
    private UIThread uiThread;
    private final static int FLAG_WAV = 0;
    private int mState = -1;    //-1:没再录制，0：录制wav，1：录制amr
    private final static int CMD_RECORDING_TIME = 2000;
    private final static int CMD_RECORDFAIL = 2001;
    private final static int CMD_STOP = 2002;
    private ProgressDialog pd;
    private VoiceSerice voiceSerice;
    private VoicecConnection conn = null;
    private SoapWeb2 soapweb;
    private int sum=0;
    public String configSetName = Constact.configSetName;//识别模式
    String spokenText = "在小i,我的声音就是我的密码"; // Note: this is optional in Text Dependent but mandatory in Text Prompted.
    String speakerID = "Speaker1";//身份
    String voiceprintTag;
    VoiceprintTagSharedPreferences voiceprintTagSharedPreferences;
    private UserSharedPreferences userSharedPreferences;
    public AuthenticationActivty() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
       // PhoneHead.HeadStyle(this);

        setContentView(R.layout.authentication);
        inflater = LayoutInflater.from(this);
        intiMicDialog();
        MicDialog();
        microphone = (ImageView) this.findViewById(R.id.microphone);
        esc_text = (TextView) this.findViewById(R.id.esc_text);
        esc_text.setOnClickListener(this);
        microphone.setOnClickListener(this);
        uiHandler = new UIHandler();
        conn = new VoicecConnection();
        getApplicationContext().bindService(new Intent(AuthenticationActivty.this, VoiceSerice.class), conn, BIND_AUTO_CREATE);
        soapweb=new SoapWeb2();
        userSharedPreferences=new UserSharedPreferences(this);
        speakerID=userSharedPreferences.getspeakerID();
        voiceprintTagSharedPreferences=new VoiceprintTagSharedPreferences(this);
        voiceprintTag=speakerID;
        Log.i("vpm",speakerID+"....");
        Log.i("vpm",voiceprintTag+"....");
        sum=0;
    }

    /**
     * 找MicDialog的控件用
     */
    private void intiMicDialog(){
        MicDialogview = inflater.inflate(R.layout.dilog_muban, null);
        startORstop = (Button) MicDialogview.findViewById(R.id.startORstop);
        cancle = (Button) MicDialogview.findViewById(R.id.cancle);
        listenbnt = (Button) MicDialogview.findViewById(R.id.listenbnt);
        push= (Button) MicDialogview.findViewById(R.id.push);
    }


    public void MicDialog(){
        biuBuilder = new AlertDialog.Builder(AuthenticationActivty.this);
        create = biuBuilder.create();
        biuBuilder.setCancelable(false);
        biuBuilder.setOnKeyListener(new MyDialogKey());
        create.setCancelable(false);



        create.setView(MicDialogview);
        // create.show();
        startORstop.setOnClickListener(AuthenticationActivty.this);
        push.setOnClickListener(this);
        listenbnt.setVisibility(View.GONE);
        push.setVisibility(View.GONE);
        cancle.setOnClickListener(AuthenticationActivty.this);
        listenbnt.setOnClickListener(AuthenticationActivty.this);
    }
    /**
     * dialog返回键的监听
     */
    private class MyDialogKey implements DialogInterface.OnKeyListener {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.microphone:
                listenbnt.setVisibility(View.GONE);
                push.setVisibility(View.GONE);
                create.show();
                break;
            case R.id.startORstop:
                if (startORstop.getText().toString().equals("开始录音")) {
                    //已经完成声纹录音第0个（共四个
                    push.setVisibility(View.GONE);
                    listenbnt.setVisibility(View.GONE);
                    push.setText("验证");
                    record(FLAG_WAV);
                    Toast.makeText(AuthenticationActivty.this, "已经开始录制，请根据上方提示说话", Toast.LENGTH_SHORT).show();
                    startORstop.setText("结束录音");

                } else {
                    push.setVisibility(View.VISIBLE);
                    listenbnt.setVisibility(View.VISIBLE);
                    stop();
                    startORstop.setText("开始录音");
                }
                break;
            case R.id.cancle:
                startORstop.setText("开始录音");
                stop();
                voiceSerice.stopVoice();
                push.setVisibility(View.GONE);
                listenbnt.setVisibility(View.GONE);
                if (audiofile != null) {
                    audiofile.delete();
                    audiofile=null;
                }
                create.dismiss();
                break;
            case R.id.push:
                if (audiofile!=null && audiofile.length()>0){
                    voiceSerice.stopVoice();
                    VerifyAsyncTask verifyAsyncTask = new VerifyAsyncTask(speakerID, voiceprintTag, audiofile);
                    verifyAsyncTask.execute();
                    create.dismiss();
                }else{
                    Toast.makeText(AuthenticationActivty.this,"文件没有准备好，请稍后点击",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.listenbnt:
                if (listenbnt.getText().equals("播放")) {

                    if(audiofile !=null && !audiofile.getAbsolutePath().equals("")){
                        Toast.makeText(AuthenticationActivty.this,"你点击了播放按钮，请聆听你的声音",Toast.LENGTH_SHORT).show();
                        voiceSerice.play(audiofile.getAbsolutePath());
                        listenbnt.setText("停止");
                    }else{
                        Toast.makeText(AuthenticationActivty.this,"您的录音文件问准备好，请稍后再点击",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    voiceSerice.stopVoice();
                    listenbnt.setText("播放");
                }
                break;
            case R.id.esc_text:
                voiceSerice.stopVoice();
                this.finish();
                break;
            default:
                break;
        }
    }

    class UIHandler extends Handler {
        public UIHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.d("MyHandler", "handleMessage......");
            super.handleMessage(msg);
            Bundle b = msg.getData();
            int vCmd = b.getInt("cmd");
            switch (vCmd) {
                case CMD_RECORDING_TIME:
                    int vTime = b.getInt("msg");
                    break;
                case CMD_RECORDFAIL:
                    int vErrorCode = b.getInt("msg");
                    String vMsg = ErrorCode.getErrorInfo(AuthenticationActivty.this, vErrorCode);
                    Toast.makeText(AuthenticationActivty.this, "录音失败：" + vMsg, Toast.LENGTH_SHORT).show();
                    break;
                case CMD_STOP:
                    int vFileType = b.getInt("msg");
                    switch (vFileType) {
                        case FLAG_WAV:
                            AudioRecordFunc mRecord_1 = AudioRecordFunc.getInstance();
                            long mSize = mRecord_1.getRecordFileSize();
                            Toast.makeText(AuthenticationActivty.this, "录音已停止.录音文件:" + AudioFileFunc.getWavFilePath() + "\n文件大小：" + mSize, Toast.LENGTH_SHORT).show();
                            audiofile = new File(AudioFileFunc.getWavFilePath());

                    }
                    break;
                default:
                    break;
            }
        }
    }

    ;

    class UIThread implements Runnable {
        int mTimeMill = 0;
        boolean vRun = true;
        public void stopThread() {
            vRun = false;
        }

        public void run() {
            while (vRun) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mTimeMill++;
                Log.d("thread", "mThread........" + mTimeMill);
                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
                b.putInt("cmd", CMD_RECORDING_TIME);
                b.putInt("msg", mTimeMill);
                msg.setData(b);
                AuthenticationActivty.this.uiHandler.sendMessage(msg); //向Handler发送消息,更新UI
            }

        }
    }

    /**
     * 开始录音
     *
     * @param mFlag，0：录制wav格式，1：录音amr格式
     */
    private void record(int mFlag) {
        if (mState != -1) {
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd", CMD_RECORDFAIL);
            b.putInt("msg", ErrorCode.E_STATE_RECODING);
            msg.setData(b);

            uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
            return;
        }
        int mResult = -1;
        switch (mFlag) {
            case FLAG_WAV:
                AudioRecordFunc mRecord_1 = AudioRecordFunc.getInstance();
                mResult = mRecord_1.startRecordAndFile();
                break;
        }
        if (mResult == ErrorCode.SUCCESS) {
            uiThread = new UIThread();
            new Thread(uiThread).start();

            mState = mFlag;
        } else {
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd", CMD_RECORDFAIL);
            b.putInt("msg", mResult);
            msg.setData(b);

            uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
        }
    }

    /**
     * 停止录音
     */
    private void stop(){
        if (mState != -1) {
            switch (mState) {
                case FLAG_WAV:
                    AudioRecordFunc mRecord_1 = AudioRecordFunc.getInstance();
                    mRecord_1.stopRecordAndFile();
                    break;
            }
            if (uiThread != null) {
                uiThread.stopThread();
            }
            if (uiHandler != null)
                uiHandler.removeCallbacks(uiThread);
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd", CMD_STOP);
            b.putInt("msg", mState);
            msg.setData(b);
            uiHandler.sendMessageDelayed(msg, 1000); // 向Handler发送消息,更新UI
            mState = -1;
        }
    }


    private class VoicecConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            VoiceSerice.VoiceBinder binder = (VoiceSerice.VoiceBinder) iBinder;
            voiceSerice = binder.getVoiceService();
           // Toast.makeText(AuthenticationActivty.this, "绑定成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //Toast.makeText(AuthenticationActivty.this, "绑定失败", Toast.LENGTH_SHORT).show();
        }
    }

    private class VerifyAsyncTask extends AsyncTask<Void,Void,Result> {
        String speakerID;
        String voiceprintTag;
        File audiofile;
        public VerifyAsyncTask(String speakerID, String voiceprintTag, File audiofile) {
            this.speakerID = speakerID;
            this.voiceprintTag = voiceprintTag;
            this.audiofile = audiofile;
            pd = ProgressDialog.show(AuthenticationActivty.this, null, "正在验证，请稍等");
        }

        @Override
        protected Result doInBackground(Void... voids) {
            sum=sum+1;
            return verify(speakerID,voiceprintTag,audiofile);
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            History history=new History();
            LinkedList<History> list=new LinkedList<History>();
            list.clear();
            pd.dismiss();
            String dec=result.getDecision();
            if(!dec.equals("Match")){
                //falseDialog(dec.value().toString());
                history.setStyle(true);
                history.setResult(false);
                history.setTime(new Date());
                history.setMoney("1200");
                saveHistory(list,history);
                if (sum<=3){
                    VerifyDialog(dec,false);
                }else{
                    Toast.makeText(AuthenticationActivty.this,"验证失败",Toast.LENGTH_SHORT).show();
                    AuthenticationActivty.this.finish();
                }



            }else{
                Toast.makeText(AuthenticationActivty.this,"转账成功",Toast.LENGTH_SHORT).show();
                history.setStyle(true);
                history.setResult(true);
                history.setTime(new Date());
                history.setMoney("1200");
                saveHistory(list,history);
                Intent intent=new Intent(AuthenticationActivty.this,Ransfersucceed.class);
                startActivity(intent);
                AuthenticationActivty.this.finish();
            }


        }
    }

    public void saveHistory(LinkedList<History> list,History history){
        list.clear();
        if (userSharedPreferences.getHistory(speakerID)!=null && !userSharedPreferences.getHistory(speakerID).equals("")){

            list.addAll(userSharedPreferences.getHistory(speakerID));
        }
        list.addFirst(history);
        userSharedPreferences.putHistory(list,speakerID);
    }

    private SessionInfo startSession() {
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

    private void endSession(SessionInfo sessionInfo) {
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

    private Result verify(String speakerID, String voiceprintTag, File audiofile) {
        SessionInfo sessionInfo = startSession();
        Result vResult = new Result();
        try {
            String StartAudioResult = soapweb.startAudio(sessionInfo.getSessionId(), configSetName).toString();
            Log.i("tree", StartAudioResult);
            soapweb.addAudio(sessionInfo.getSessionId(), audiofile, StartAudioResult, configSetName);
            soapweb.endAudio(sessionInfo.getSessionId(), StartAudioResult, configSetName);
            SoapObject resultSoapObject = soapweb.verify(sessionInfo.getSessionId(), speakerID, voiceprintTag, StartAudioResult, spokenText, configSetName);
//            vResult.setRequestId(Long.parseLong(resultSoapObject.getProperty("RequestId").toString()));
//            vResult.setAudioSegmentId(Long.parseLong(resultSoapObject.getProperty("AudioSegmentId").toString()));
            vResult.setDecision(resultSoapObject.getProperty("Decision").toString());
//            vResult.setDecisionReason(resultSoapObject.getProperty("DecisionReason").toString());
//            vResult.setAdditionalInfo((String) resultSoapObject.getProperty("AdditionalInfo"));
//            vResult.setSpeakerId((String) resultSoapObject.getProperty("SpeakerId"));
//            vResult.setWatchListSuspect((String) resultSoapObject.getProperty("WatchListSuspect"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            endSession(sessionInfo);
        }
        return vResult;
    }

    /**
     * 验证弹出来的弹框
     */
    private void VerifyDialog(String reason, boolean trueOrfalse) {
//        Intent intent=new Intent(AuthenticationActivty.this,PaymentActivty.class);
//        AuthenticationActivty.this.finish();
        String bntText = "验证";
        String Message=reason + "验证没成功，没关系！请继续验证,你有三次机会，现在是第"+sum+"次";
        if (!trueOrfalse) {
            bntText = "再次验证";
        }
        if (sum==3){
            bntText="返回";
            Message="你已经验证失败三次了，请返回支付界面核对好您的信息";
        }
        AlertDialog.Builder biuBuilder = new AlertDialog.Builder(AuthenticationActivty.this);
        biuBuilder.setMessage(Message);
        biuBuilder.setPositiveButton(bntText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
//                    VerifyAsyncTask verifyAsyncTask=new VerifyAsyncTask(speakerID,voiceprintTag,audiofile);
//                    verifyAsyncTask.execute();
                //MicDialog(false);
                if (sum==3){
                       AuthenticationActivty.this.finish();
                }
                push.setVisibility(View.GONE);
                listenbnt.setVisibility(View.GONE);
                push.setText("验证");
                create.show();
                dialog.dismiss();

            }
        });
        biuBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (sum==3){
                    AuthenticationActivty.this.finish();
                }
                dialog.dismiss();
            }
        });
        push.setVisibility(View.GONE);
        listenbnt.setVisibility(View.GONE);
        biuBuilder.setOnKeyListener(new MyDialogKey());
        biuBuilder.setCancelable(false);
        biuBuilder.create().show();
    }

    @Override
    protected void onDestroy(){
        if (conn != null) {
            voiceSerice.stopVoice();
            getApplicationContext().unbindService(conn);
        }
        super.onDestroy();
    }

}
