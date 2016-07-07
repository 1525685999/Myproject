package com.com.xiaoi.serve;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

public class VoiceSerice extends Service {
    MediaPlayer mediaPlayer;
    String song;
    //SeekBar seekBar;
    Handler handler;
    Boolean flag=false;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new VoiceBinder();
    }
    public class VoiceBinder extends Binder {
        public VoiceSerice getVoiceService(){
            return VoiceSerice.this;
        }
    }

//    public void setSeekBar(final SeekBar seekBar) {
//        this.seekBar = seekBar;
//        seekBar.setEnabled(false); //设置成不可用的。
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            @Override
//            public void onStopTrackingTouch(SeekBar arg0) {
//                // TODO Auto-generated method stub
//                if(mediaPlayer!=null){
//                    mediaPlayer.seekTo(seekBar.getProgress());
//                }
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar arg0) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//        handler=new Handler(){
//
//            @Override
//            public void handleMessage(Message msg) {
//                // TODO Auto-generated method stub
//                switch (msg.what) {
//                    case 0:
//                        if(mediaPlayer!=null){
//                            seekBar.setProgress(mediaPlayer.getCurrentPosition());  //播放跟我们的seekbar同步
//                        }
//                        break;
//
//                    default:
//                        break;
//                }
//
//            }
//
//        };
//
//    }
    //播放
    public void play(final String song){
        if(mediaPlayer!=null){
            mediaPlayer.start();
        }else{
            this.song=song;
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            try {
                //指定要播放的文件的真实路径
                mediaPlayer.setDataSource(song);
                mediaPlayer.prepare();

                int max=mediaPlayer.getDuration();//这个音频文件的总时长
                int cur=mediaPlayer.getCurrentPosition(); //得到当前文件放到了哪个时间段了。
                flag=true;
//                seekBar.setEnabled(flag);
//                seekBar.setMax(max);
//                seekBar.setProgress(cur);


                mediaPlayer.start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Toast.makeText(this, "播放失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //当音频播放完了之后，会触发这个事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer arg0) {
                    // TODO Auto-generated method stub
                    stopVoice();
                    play(song);
//                    seekBar.setEnabled(true);
                }
            });
            //当音频播放出现错误的时候
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(VoiceSerice.this, "音频文件错误！！！",Toast.LENGTH_SHORT).show();
                    stopVoice();
                    return false;
                }
            });
        }
        flag=true;
        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {
                while(flag){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i("TAG", Thread.currentThread().getName()+"发送了一个信息！！！！");
                    Message messge = new  Message();
                    messge.what = 0;
   //                 handler.sendMessage(messge);
                }

            }
        });
        th.start();
    }


    //暂停
    public void pause(){
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }
    //重播
    public void repaly(){
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(0);
        }else if(mediaPlayer!=null){
            mediaPlayer.seekTo(0);
            play(song);
        }else{
            play(song);
        }
    }
    //停止
    public void stopVoice(){
        flag = false;
//        seekBar.setProgress(0);
//        seekBar.setEnabled(false);
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }





}
