package com.cuhk.ieproject;

import android.content.Context;

/**
 * Created by Felix on 2017-03-31.
 */

public class Check {
    public static final String trueID = "8";

    public Check(Context context){

    }

    public boolean verifyUser(String id){
        if(id. equals(trueID)){
            return true;
        } else{
            return false;
        }
    }
}