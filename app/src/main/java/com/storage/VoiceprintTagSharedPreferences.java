package com.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 2016/5/9.
 */
public class VoiceprintTagSharedPreferences {
       Context context;
       SharedPreferences sharedPreferences;
    public VoiceprintTagSharedPreferences(Context context) {
        this.context = context;
        this.sharedPreferences=context.getSharedPreferences("voiceprintTag", Context.MODE_PRIVATE);
    }
   public void putVoiceprintTag(String voiceprintTag,String speakerID){
       SharedPreferences.Editor editor=sharedPreferences.edit();
       editor.putString(speakerID,voiceprintTag);
       editor.commit();
   }
   public String getVoiceprintTag(String speakerID){
       String  voiceprintTag=sharedPreferences.getString(speakerID,"");
       return voiceprintTag;
   }
    public void putVoiceprintTagResult(String voiceprintTagResult,String speakerID){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(speakerID+"Result",voiceprintTagResult);
        editor.commit();
    }
    public String getVoiceprintTagResult(String speakerID){
        String  voiceprintTag=sharedPreferences.getString(speakerID+"Result","");
        return voiceprintTag;
    }
}
