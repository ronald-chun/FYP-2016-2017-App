package com.cuhk.ieproject.model;

import android.content.Context;

import com.android.volley.Response;
import com.cuhk.ieproject.util.RequestWorker;

import org.json.JSONObject;

import java.util.HashMap;

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

    public static class LoginRequest extends RequestWorker.PostRequest<JSONObject> {
        private static final String path = "/user/8";

        public LoginRequest(final Context context, Response.Listener<JSONObject> listener, RequestWorker.ErrorListener errorListener){
            super(context, path, new HashMap<String, String>(){{
            }}, listener, errorListener);
        }

        @Override
        public JSONObject convertResponse(JSONObject jsonObject) {
            JSONObject resultObj = null;
            resultObj = jsonObject;
            return resultObj;
        }
    }
}
