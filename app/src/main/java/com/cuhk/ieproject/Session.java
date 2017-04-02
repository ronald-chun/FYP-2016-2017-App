package com.cuhk.ieproject;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Felix on 2017-03-31.
 */

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;
    private Check check;

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("app", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public void setLoggedin(boolean loggedin){
        editor.putBoolean("LoggedInmode", loggedin);
        editor.commit();
    }

    public boolean loggedin(){
        return prefs.getBoolean("LoggedInmode", false);
    }
}