package com.cuhk.ieproject.util;

import android.os.Build;

import com.cuhk.ieproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anson on 1/9/2016.
 */
public class Error {
    private String code = "SYS001";
    private String message = "System Error.";
    private String details = "";
    private Integer messageForDisplay = R.string.hello_world;

    private static final Map<String, Integer> code2StringIdMap = new HashMap<String, Integer>(){{
        put("AU001", R.string.hello_world);

    }};

    public Error(String code, String message) {
        this.code = code;
        this.message = message;
        if (Build.VERSION.SDK_INT >= 24) {
            this.messageForDisplay =  messageForDisplay;
        }else {
            this.messageForDisplay = (code2StringIdMap.get(code) != null ? code2StringIdMap.get(code) : messageForDisplay);
        }
    }

    public Error(JSONObject jsonObject) {
        try {
            this.code = jsonObject.getString("error_code");
            this.message = jsonObject.getString("error");
            this.details = jsonObject.optString("error_detail");
            if (Build.VERSION.SDK_INT >= 24) {
                this.messageForDisplay =  messageForDisplay;
            }else {
                this.messageForDisplay = (code2StringIdMap.get(code) != null ? code2StringIdMap.get(code) : messageForDisplay);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Error() {

    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getMessageForDisplay() {
        return messageForDisplay;
    }

    public String getDetails() {
        return details;
    }
}
