package com.cuhk.ieproject;

import android.content.SharedPreferences;
import android.content.Context;

/**
 * Created by Felix on 2017-04-01.
 */

public class Setting {
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    Context ctx;
    private static final String data = "DATA";

    public Setting(Context ctx){
        this.ctx = ctx;
        settings = ctx.getSharedPreferences(data, 0);
        editor = settings.edit();
    }

//    int uid;
//    int pref;
//    String uname;
//    String utscore;
//    String sid;
//    String description;

    public void setUID(int uid){
        editor.putInt("uid", uid);
        editor.commit();
    }

    public int getUID(){
        return settings.getInt("uid", -1);
    }

    public void setPref(int pref){
        editor.putInt("pref", pref);
        editor.commit();
    }

    public int getPref(){
        return settings.getInt("pref", -1);
    }

    public void setUsername(String username){
        editor.putString("username", username);
        editor.commit();
    }

    public String getUsername(){
        return settings.getString("username", "");
    }

    public void setUtsore(String utscore){
        editor.putString("utscore", utscore);
        editor.commit();
    }

    public String getUtsore(){
        return settings.getString("utscore", "");
    }

    public void setSID(String sid){
        editor.putString("sid", sid);
        editor.commit();
    }

    public String getSID(){
        return settings.getString("sid", "");
    }

    public void setDescription(String description){
        editor.putString("description", description);
        editor.commit();
    }

    public String getDescription(){
        return settings.getString("description", "");
    }


    public void setEnableCamera(boolean enableCamera){
        editor.putBoolean("enableCameraMode", enableCamera);
        editor.commit();
    }

    public boolean camera() {
        return settings.getBoolean("enableCameraMode", false);
    }

    public void setCardSize(int cardSize){
        editor.putInt("cardSize", cardSize);
        editor.commit();
    }

    public int getCardSize(){
        return settings.getInt("cardSize", 4);
    }

    public void setEnableContext(boolean enableContext){
        editor.putBoolean("enableContext", enableContext);
        editor.commit();
    }

    public boolean context() {
        return settings.getBoolean("enableContext", false);
    }
}


