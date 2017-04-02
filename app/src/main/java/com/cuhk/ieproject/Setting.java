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

    public void setUsername(String username){
        editor.putString("username", username);
        editor.commit();
    }

    public String getUsername(){
        return settings.getString("username", "");
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
}


