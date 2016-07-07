package com.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.modle.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by user on 2016/5/9.
 */
public class UserSharedPreferences {
       Context context;
       SharedPreferences sharedPreferences;
       private LinkedList hlist=new LinkedList<History>();
        public UserSharedPreferences(Context context) {
        this.context = context;
        this.sharedPreferences=context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }
   public void putUserName(String UserName){
       SharedPreferences.Editor editor=sharedPreferences.edit();
       editor.putString(UserName,UserName);
       editor.putString("speakerID",UserName);
       editor.commit();
   }
    public void putspeakerID(String UserName){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("VBspeakerID",UserName);
        editor.commit();
    }
    public String getspeakerID(){
        String  speakerID=sharedPreferences.getString("VBspeakerID","");
        return speakerID;
    }
   public String getUserName(String userName){
       String  username=sharedPreferences.getString(userName,"");
       return username;
   }
   public void putPassword(String password){
       SharedPreferences.Editor editor=sharedPreferences.edit();
       editor.putString("password",password);
       editor.commit();
   }
    public String getPassword(){
        String  password=sharedPreferences.getString("password","");
        return password;
    }

    public void putHistory(List<History> list,String speakerID){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(speakerID+"Hlist",list.toString());
        editor.commit();
    }

    public List getHistory(String speakerID){
        hlist.clear();
        String  list=sharedPreferences.getString(speakerID+"Hlist","");
//        if(!list.equals("")){
//            hlist= JSON.parseArray(list,History.class);
//        }
        JSONTokener jt = new JSONTokener(list);
        try {
            JSONArray jsonArr = (JSONArray) jt.nextValue();
            for (int i = 0; i < jsonArr.length(); i++) {
                History h=new History();
                JSONObject jsonObj = jsonArr.getJSONObject(i);//获取每一项JSONObject 之后，就可以获取里面的值。
//                private boolean style;//true为转账，false为验证
//                private boolean result;//true成功false为失败
//                private String time;
//                private String money;
                h.setMoney(jsonObj.getString("money"));
                h.setTime(jsonObj.getString("time"));
                h.setStyle(jsonObj.getBoolean("style"));
                h.setResult(jsonObj.getBoolean("result"));
                hlist.add(h);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hlist;
    }

}
