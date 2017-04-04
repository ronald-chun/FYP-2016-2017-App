package com.cuhk.ieproject;

import android.content.Context;

/**
 * Created by Felix on 2017-03-31.
 */

public class Check {
    public static final String trueID = "abc";
    public static final String truePass = "abc";

    public Check(Context context){

    }

    public boolean verifyUser(String id, String password){
        if(id. equals(trueID) && password.equals(truePass)){
            return true;
        } else{
            return false;
        }
    }
}