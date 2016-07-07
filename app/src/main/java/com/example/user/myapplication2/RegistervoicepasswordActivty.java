package com.example.user.myapplication2;


import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

import com.Style.PhoneHead;
import com.Time.GetTime;
import com.com.xiaoi.sample.EnrollStatusExt;
import com.com.xiaoi.serve.AudioFileFunc;
import com.com.xiaoi.serve.AudioRecordFunc;
import com.com.xiaoi.serve.ErrorCode;
import com.com.xiaoi.serve.VoiceSerice;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.modle.Decision;
import com.modle.DecisionReason;
import com.modle.EnrollResult;
import com.modle.EnrollStatus;
import com.modle.History;
import com.modle.InvalidityReason;
import com.modle.Result;
import com.modle.SessionInfo;
import com.modle.TrainReason;
import com.modle.TrainResult;
import com.modle.TrainStatus;
import com.storage.Constact;
import com.storage.UserSharedPreferences;
import com.storage.VoiceprintTagSharedPreferences;
import com.xiaoi.mywebserice.SoapWeb2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Html;
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

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

public class RegistervoicepasswordActivty extends Activity implements OnClickListener {
    private ImageView microphone;
    private AlertDialog create;
    private AlertDialog.Builder biuBuilder;//弹框
    private LayoutInflater inflater;
    private Button startORstop, cancle, listenbnt, push;
    private TextView userName;
    private TextView num;
    int sum;
    private final static int FLAG_WAV = 0;
    private int mState = -1;    //-1:没再录制，0：录制wav，1：录制amr
    private UIHandler uiHandler;
    private UIThread uiThread;
    private final static int CMD_RECORDING_TIME = 2000;
    private final static int CMD_RECORDFAIL = 2001;
    private final static int CMD_STOP = 2002;
    private ProgressDialog pd;
    private File audiofile;
    private TextView esc_text;
    private VoicecConnection conn = null;
    private VoiceSerice voiceSerice;
    private SoapWeb2 soapweb;
    View MicDialogview;


    public String configSetName = Constact.configSetName;//识别模式
    String spokenText = "在小i,我的声音就是我的密码"; // Note: this is optional in Text Dependent but mandatory in Text Prompted.
    String speakerID = "Speaker2";//身份
    String voiceprintTag;
    VoiceprintTagSharedPreferences voiceprintTagSharedPreferences;
    private UserSharedPreferences userSharedPreferences;
    private boolean againEnorll=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //PhoneHead.HeadStyle(this);

        setContentView(R.layout.registervoicepassword);
        sum = 0;
        userName= (TextView) findViewById(R.id.userName);
        userSharedPreferences = new UserSharedPreferences(this);
        if (!userSharedPreferences.getspeakerID().equals("")){
            speakerID=userSharedPreferences.getspeakerID();
            userName.setText(speakerID=userSharedPreferences.getspeakerID());
        }else{
            RegistervoicepasswordActivty.this.finish();
            Toast.makeText(this,"该用户没有注册",Toast.LENGTH_SHORT).show();
            this.finish();
        }
        soapweb = new SoapWeb2();
        inflater = LayoutInflater.from(this);
        voiceprintTagSharedPreferences = new VoiceprintTagSharedPreferences(this);
        voiceprintTag = speakerID;
        intiMicDialog();
        MicDialog();
//        if (!voiceprintTag.equals("")){
//            voiceprintTagSharedPreferences.putVoiceprintTag("MyVoiceprint"+GetTime.getTime(new Date()),speakerID);
//            voiceprintTag =voiceprintTagSharedPreferences.getVoiceprintTag(speakerID);
//        } else {
//            voiceprintTag ="MyVoiceprint"+GetTime.getTime(new Date());
//            voiceprintTagSharedPreferences.putVoiceprintTag(voiceprintTag,speakerID);
//        }
//        voiceprintTagSharedPreferences.putVoiceprintTagResult(speakerID+"_"+"false",speakerID);

        microphone = (ImageView) this.findViewById(R.id.microphone);
        esc_text = (TextView) findViewById(R.id.esc_text);
        num = (TextView) this.findViewById(R.id.num);
        num.setText(Html.fromHtml("已经完成声纹录音第<font color=red>" + sum + "</font>个（共<font color=red>" + "三" + "</font>个）"));
        microphone.setOnClickListener(this);
        esc_text.setOnClickListener(this);


        conn = new VoicecConnection();
        getApplicationContext().bindService(new Intent(RegistervoicepasswordActivty.this, VoiceSerice.class), conn, BIND_AUTO_CREATE);
        uiHandler = new UIHandler();

    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    /**
     * 找MicDialog的控件用
     */
    private void intiMicDialog() {
        MicDialogview = inflater.inflate(R.layout.dilog_muban, null);
        startORstop = (Button) MicDialogview.findViewById(R.id.startORstop);
        cancle = (Button) MicDialogview.findViewById(R.id.cancle);
        listenbnt = (Button) MicDialogview.findViewById(R.id.listenbnt);
        push = (Button) MicDialogview.findViewById(R.id.push);
    }

    /**
     * 验证用的dialog
     * true为注册用，false为验证用
     */

    public void MicDialog() {
        biuBuilder = new AlertDialog.Builder(RegistervoicepasswordActivty.this);
        create = biuBuilder.create();
        biuBuilder.setCancelable(false);
        biuBuilder.setOnKeyListener(new MyDialogKey());
        create.setCancelable(false);


        create.setView(MicDialogview);
        // create.show();
        startORstop.setOnClickListener(RegistervoicepasswordActivty.this);
        push.setOnClickListener(this);
        push.setVisibility(View.GONE);
        cancle.setOnClickListener(RegistervoicepasswordActivty.this);
        listenbnt.setOnClickListener(RegistervoicepasswordActivty.this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.microphone:
                create.show();
                break;
            case R.id.startORstop:
                voiceSerice.stopVoice();
                listenbnt.setText("播放");
                if (startORstop.getText().toString().equals("开始录音")) {
                    //已经完成声纹录音第0个（共四个
                    push.setVisibility(View.GONE);
                    listenbnt.setVisibility(View.GONE);
                    record(FLAG_WAV);
                    Toast.makeText(RegistervoicepasswordActivty.this, "已经开始录制，请根据上方提示说话", Toast.LENGTH_SHORT).show();
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
                voiceSerice.stopVoice();
                if (audiofile != null) {
                    audiofile.delete();
                    audiofile=null;
                }
                listenbnt.setText("播放");
                create.dismiss();
                break;
            case R.id.esc_text:
                voiceSerice.stopVoice();
                RegistervoicepasswordActivty.this.finish();
                break;
            case R.id.listenbnt:
                if (audiofile != null && !audiofile.getAbsolutePath().equals("")) {
                    if (listenbnt.getText().equals("播放")) {
                        Toast.makeText(RegistervoicepasswordActivty.this, "你点击了播放按钮，请聆听你的声音", Toast.LENGTH_SHORT).show();
                        voiceSerice.play(audiofile.getAbsolutePath());
                        listenbnt.setText("停止");
                    } else {
                        voiceSerice.stopVoice();
                        listenbnt.setText("播放");
                    }
                }else{
                    Toast.makeText(RegistervoicepasswordActivty.this, "没有播放文件", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.push:
                voiceSerice.stopVoice();
                if (push.getText().toString().equals("注册") && audiofile != null && !audiofile.getAbsolutePath().equals("")) {
                    new EnrollAsyncTask(speakerID, voiceprintTag, audiofile).execute();
                    create.dismiss();
                } else if(push.getText().toString().equals("验证") && audiofile != null && !audiofile.getAbsolutePath().equals("")){
                    VerifyAsyncTask verifyAsyncTask = new VerifyAsyncTask(speakerID, voiceprintTag, audiofile);
                    verifyAsyncTask.execute();
                    create.dismiss();
                }else{
                    Toast.makeText(RegistervoicepasswordActivty.this, "文件尚未准备好", Toast.LENGTH_SHORT).show();
                }

                //audiofile = new File(AudioFileFunc.getWavFilePath());

                break;
            default:
                break;
        }
    }

    /**
     * dialog返回键的监听
     */
    private class MyDialogKey implements OnKeyListener {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 注册成功与失败弹出来的dialog
     */
    private void EnrollDialog(String reason, boolean TrueORFalse, final boolean ifverfiy) {
        AlertDialog.Builder ebiuBuilder = new AlertDialog.Builder(RegistervoicepasswordActivty.this);
        ebiuBuilder.setTitle("提示"); //设置标题
        ebiuBuilder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        if (TrueORFalse) {
            reason = "注册成功,但是至少要注册三次哦";
        }
        ebiuBuilder.setMessage(reason);
        ebiuBuilder.setPositiveButton("再次录音", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();

                //Toast.makeText(RegistervoicepasswordActivty.this, "录音开始", Toast.LENGTH_SHORT).show();
                push.setVisibility(View.GONE);
                listenbnt.setVisibility(View.GONE);
                create.show();
                // startORstop.setText("开始录音");
                dialog.cancel();
            }
        });

        ebiuBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (ifverfiy){
                    RegistervoicepasswordActivty.this.finish();
                }else{
                    dialog.cancel();
                    push.setVisibility(View.GONE);
                    listenbnt.setVisibility(View.GONE);
                }
            }
        });
        ebiuBuilder.setOnKeyListener(new MyDialogKey());
        ebiuBuilder.setCancelable(false);
        ebiuBuilder.create().show();
    }

    /**
     * 验证弹出来的弹框
     */
    private void VerifyDialog(String reason, boolean trueOrfalse) {
        String bntText = "验证";
        if (!trueOrfalse) {
            bntText = "再次验证";
        }
        AlertDialog.Builder biuBuilder = new AlertDialog.Builder(RegistervoicepasswordActivty.this);
        if("恭喜验证通过".equals(reason)){
            biuBuilder.setTitle(reason); //设置标题
        }else{
            biuBuilder.setTitle("提示"); //设置标题
        }

        biuBuilder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        biuBuilder.setMessage(reason + " 你可以点击再次验证，也可以点击继续注册来提高验证率,也可以点击主页来进入主页");
        biuBuilder.setPositiveButton(bntText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
//                    VerifyAsyncTask verifyAsyncTask=new VerifyAsyncTask(speakerID,voiceprintTag,audiofile);
//                    verifyAsyncTask.execute();

                //MicDialog(false);
                push.setVisibility(View.GONE);
                listenbnt.setVisibility(View.GONE);
                push.setText("验证");
                create.show();
                dialog.dismiss();

            }
        });
        biuBuilder.setNegativeButton("继续注册", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();

                push.setVisibility(View.GONE);
                listenbnt.setVisibility(View.GONE);
                audiofile.delete();
                audiofile=null;
                push.setText("注册");
               // EnrollDialog("再次注册来提高验证率", false,true);
                create.show();

            }
        });
        biuBuilder.setNeutralButton("主页", new DialogInterface.OnClickListener() {//设置忽略按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(RegistervoicepasswordActivty.this,AccountrActivty.class);
                startActivity(intent);
                RegistervoicepasswordActivty.this.finish();
                dialog.dismiss();
            }
        });

        biuBuilder.setOnKeyListener(new MyDialogKey());
        biuBuilder.setCancelable(false);
        biuBuilder.create().show();

    }
/**
 * 验证成功
 */
public void Successful(){
    push.setText("注册");
    AlertDialog.Builder ebiuBuilder = new AlertDialog.Builder(RegistervoicepasswordActivty.this);
    ebiuBuilder.setTitle("提示"); //设置标题
    ebiuBuilder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
    ebiuBuilder.setMessage("恭喜你验证通过，你是打算继续注册来提高验证通过率呢，还是进入主页");
    ebiuBuilder.setPositiveButton("再次录音", new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            dialog.dismiss();
            //Toast.makeText(RegistervoicepasswordActivty.this, "录音开始", Toast.LENGTH_SHORT).show();
            push.setVisibility(View.GONE);
            listenbnt.setVisibility(View.GONE);
            create.show();
            // startORstop.setText("开始录音");
        }
    });

    ebiuBuilder.setNegativeButton("主页", new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
//            if (ifverfiy){
//                RegistervoicepasswordActivty.this.finish();
//            }else{
//                dialog.cancel();
//                push.setVisibility(View.GONE);
//                listenbnt.setVisibility(View.GONE);
//            }
            Intent intent=new Intent();
        }
    });
    ebiuBuilder.setOnKeyListener(new MyDialogKey());
    ebiuBuilder.setCancelable(false);
    ebiuBuilder.create().show();
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
            mFlag=-1;
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
    private void stop() {
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
                    String vMsg = ErrorCode.getErrorInfo(RegistervoicepasswordActivty.this, vErrorCode);
                    Toast.makeText(RegistervoicepasswordActivty.this, "录音失败：" + vMsg, Toast.LENGTH_SHORT).show();
                    break;
                case CMD_STOP:
                    int vFileType = b.getInt("msg");
                    switch (vFileType) {
                        case FLAG_WAV:
                            AudioRecordFunc mRecord_1 = AudioRecordFunc.getInstance();
                            long mSize = mRecord_1.getRecordFileSize();
                            Toast.makeText(RegistervoicepasswordActivty.this, "录音已停止.录音文件:" + AudioFileFunc.getWavFilePath() + "\n文件大小：" + mSize, Toast.LENGTH_SHORT).show();
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
                RegistervoicepasswordActivty.this.uiHandler.sendMessage(msg); //向Handler发送消息,更新UI
            }

        }
    }


    private class EnrollAsyncTask extends AsyncTask<File, Void, String> {
        String speakerID;
        String voiceprintTag;
        File audiofile;

        public EnrollAsyncTask(String speakerID, String voiceprintTag, File audiofile) {
            this.speakerID = speakerID;
            this.voiceprintTag = voiceprintTag;
            this.audiofile = audiofile;
            pd = ProgressDialog.show(RegistervoicepasswordActivty.this, null, "正在注册，请稍等");
        }

        @Override
        protected String doInBackground(File... params) {
            // TODO Auto-generated method stub
            return Register(speakerID, voiceprintTag, audiofile);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pd.dismiss();
            switch (result) {
                case "Succeeded":
                    sum = sum + 1;
                    num.setText(Html.fromHtml("已经完成声纹录音第<font color=red>" + sum + "</font>个（最少<font color=red>" + "三" + "</font>个）"));
                    // EnrollDialog("你已经注册了至少三个，可以选择继续注册来提高验证通过率",false);
                    againEnorll=true;
                    voiceprintTagSharedPreferences.putVoiceprintTagResult(speakerID+"_"+"true",speakerID);
                    VerifyDialog("注册已经有了三个", true);

                    break;
                case "MoreAudioRequired":
                    sum = sum + 1;
                    num.setText(Html.fromHtml("已经完成声纹录音第<font color=red>" + sum + "</font>个（最少<font color=red>" + "三" + "</font>个）"));
                    EnrollDialog(result, true,false);
                    if (!againEnorll){
                        voiceprintTagSharedPreferences.putVoiceprintTagResult(speakerID+"_"+"false",speakerID);
                    }
                    break;
                case "Full":
                    sum = sum + 1;
                    num.setText(Html.fromHtml("已经完成声纹录音第<font color=red>" + sum + "</font>个（最少<font color=red>" + "三" + "</font>个）"));
                    EnrollDialog(result, true,false);
                    if (!againEnorll){
                        voiceprintTagSharedPreferences.putVoiceprintTagResult(speakerID+"_"+"false",speakerID);
                    }
                    break;
                case "NotSet":
                    EnrollDialog("设置错误", false,false);
                    if (!againEnorll){
                        voiceprintTagSharedPreferences.putVoiceprintTagResult(speakerID+"_"+"false",speakerID);
                    }
                    break;
//                case TRAIN_FAIL:
//                    EnrollfalseDialog("声纹注册失败,请靠近麦克风或者到安静一点的地方重新尝试");
//                    break;
//                case SEGMENT_VALID:
//                    EnrollfalseDialog("声纹只有部分有效,请靠近麦克风或者到安静一点的地方重新尝试");
//                    break;
                default:
                    if (!againEnorll){
                        voiceprintTagSharedPreferences.putVoiceprintTagResult(speakerID+"_"+"false",speakerID);
                    }
                    EnrollDialog("声纹注册失败,请靠近麦克风或者到安静一点的地方重新尝试,原因是：" + result, false,false);
                    break;
            }
        }


    }

    private class VerifyAsyncTask extends AsyncTask<Void, Void, Result> {
        String speakerID;
        String voiceprintTag;
        File audiofile;

        public VerifyAsyncTask(String speakerID, String voiceprintTag, File audiofile) {
            this.speakerID = speakerID;
            this.voiceprintTag = voiceprintTag;
            this.audiofile = audiofile;
            pd = ProgressDialog.show(RegistervoicepasswordActivty.this, null, "正在验证，请稍等");
        }

        @Override
        protected Result doInBackground(Void... voids) {

            return verify(speakerID, voiceprintTag, audiofile);
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            History history = new History();
            LinkedList<History> list = new LinkedList<History>();
            pd.dismiss();
            String dec = result.getDecision();
            if (!dec.equals("Match")) {
          //      Toast.makeText(RegistervoicepasswordActivty.this, dec, Toast.LENGTH_SHORT).show();
                //falseDialog(dec.value().toString());
                history.setMoney("");
                VerifyDialog(dec, false);
                history.setStyle(false);
                history.setResult(false);
                history.setTime(new Date());

            } else {
                history.setMoney("");
                history.setStyle(false);
                history.setResult(true);
                history.setTime(new Date());
                Toast.makeText(RegistervoicepasswordActivty.this, "验证成功,可以点击下方麦克风继续注册录音提高验证率，也可以返回其他界面", Toast.LENGTH_LONG).show();
                audiofile.delete();
                audiofile=null;
                push.setText("注册");
                VerifyDialog("恭喜验证通过",false);
            }

            list.clear();
            if (userSharedPreferences.getHistory(speakerID)!=null && !userSharedPreferences.getHistory(speakerID).equals("")) {

                list.addAll(userSharedPreferences.getHistory(speakerID));
            }
            list.addFirst(history);
            userSharedPreferences.putHistory(list,speakerID);
        }
    }


    private class VoicecConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            VoiceSerice.VoiceBinder binder = (VoiceSerice.VoiceBinder) iBinder;
            voiceSerice = binder.getVoiceService();
           // Toast.makeText(RegistervoicepasswordActivty.this, "绑定成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //Toast.makeText(RegistervoicepasswordActivty.this, "绑定失败", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onDestroy() {
        if (conn != null) {
            voiceSerice.stopVoice();
            getApplicationContext().unbindService(conn);
        }
        super.onDestroy();
    }

    private String Register(String speakerID, String voiceprintTag, File audiofile) {
        String enrollStatusExt = null;
        SessionInfo sessionInfo = startSession();
        try {

            SoapObject enrollResultSoapObject = soapweb.enroll(sessionInfo.getSessionId(), speakerID, voiceprintTag, audiofile, spokenText, configSetName);
            EnrollResult enrollResult = new EnrollResult();


            enrollResult.setEnrollStatus(enrollResultSoapObject.getProperty("EnrollStatus").toString());
            enrollResult.setRequestId(Long.parseLong(enrollResultSoapObject.getProperty("RequestId").toString()));
            enrollResult.setAudioSegmentId(Long.parseLong(enrollResultSoapObject.getProperty("AudioSegmentId").toString()));
            enrollResult.setSegmentValid(Boolean.parseBoolean(enrollResultSoapObject.getProperty("IsSegmentValid").toString()));
            enrollResult.setReason(enrollResultSoapObject.getProperty("Reason").toString());
            enrollResult.setAdditionalInfo(enrollResultSoapObject.getProperty("AdditionalInfo").toString());
            if (enrollResult.isSegmentValid()) {
                if (enrollResult.getEnrollStatus().equals("ReadyForTraining")) {
                    SoapObject trainResultSoapObject = soapweb.train(sessionInfo.getSessionId(), speakerID, voiceprintTag, configSetName);
                    TrainResult trainResult = new TrainResult();
                    trainResult.setRequestId(Long.parseLong(trainResultSoapObject.getProperty("RequestId").toString()));
                    trainResult.setTrainStatus(trainResultSoapObject.getProperty("TrainStatus").toString());
                    trainResult.setTrainReason(trainResultSoapObject.getProperty("TrainReason").toString());
                    trainResult.setAdditionalInfo(trainResultSoapObject.getProperty("AdditionalInfo").toString());
//                    if (trainResult.getTrainStatus() =="Failed" && trainResult.getTrainStatus().equals("Failed")) {
//                        enrollStatusExt ="Failed";
//                    } else if (trainResult.getTrainStatus() =="Succeeded" && trainResult.getTrainStatus().equals("Succeeded")) {
//                        enrollStatusExt = "Succeeded";
//                    } else {
//                        enrollStatusExt ="NotSet";
//                    }
                    enrollStatusExt = trainResult.getTrainStatus();
         //           Log.i("isMyTrain",soapweb.IsTrained(sessionInfo.getSessionId(),speakerID,voiceprintTag,configSetName).toString()+"0000");
                } else {
                    enrollStatusExt = enrollResult.getEnrollStatus();
                }
            } else {
                enrollStatusExt = enrollResult.getReason();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            endSession(sessionInfo);
        }
        return enrollStatusExt;
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


}
