package com.cuhk.ieproject.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.cuhk.ieproject.util.RequestWorker;


import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anson on 7/11/2016.
 */
public class User {
    private int id;
    private String name;
    private int machinePath;
    private int sizeLevel;

    public User(int id, String name, int machinePath, int sizeLevel){
        super();
        this.id = id;
        this.name = name;
        this.machinePath = machinePath;
        this.sizeLevel = sizeLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMachinePath() {
        return machinePath;
    }

    public void setMachinePath(int machinePath) {
        this.machinePath = machinePath;
    }

    public int getSizeLevel() {
        return sizeLevel;
    }

    public void setSizeLevel(int sizeLevel) {
        this.sizeLevel = sizeLevel;
    }

    public static class TestRequest extends RequestWorker.PostRequest<JSONObject>{
        private static final String path = "/admin";

        public TestRequest(final Context context, final Response.Listener<JSONObject> listener, final RequestWorker.ErrorListener errorListener) {
            super(context, path, new HashMap<String, String>(){{
                put("1","2");
            }}, listener, errorListener);
        }

        @Override
        public JSONObject convertResponse(JSONObject jsonObject) {

            try {
                Log.v("ASD","ASDSAD");
                return jsonObject ;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
