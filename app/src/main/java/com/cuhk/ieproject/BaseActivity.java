package com.cuhk.ieproject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.cuhk.ieproject.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by anson on 5/4/2017.
 */

public class BaseActivity extends AppCompatActivity {
    protected BroadcastReceiver myReceiver;
    private Dialog loadingDialog;
    private List<UUID> loadingUUIDs = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    onTouchOutsideKeyboard(v);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    protected void onTouchOutsideKeyboard(View v) {
        v.clearFocus();
        hideKeyboard(v);
    }

    protected void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    protected void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    protected Dialog getLoadingDialog(){
        if (loadingDialog == null){
            loadingDialog = new LoadingDialog.Builder(this).build();
        }

        return loadingDialog;
    }

    protected UUID showLoadingDialog(){
        UUID uuid = UUID.randomUUID();
        showLoadingDialog(uuid);
        return uuid;
    }

    protected void showLoadingDialog(UUID uuid){
        loadingUUIDs.add(uuid);
        getLoadingDialog().show();
    }

    protected void hideLoadingDialog(UUID uuid){
        loadingUUIDs.remove(uuid);
        if (loadingUUIDs.isEmpty()){
            getLoadingDialog().dismiss();
        }
    }

    protected int getColorById(int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(id, getTheme());
        }else {
            return getResources().getColor(id);
        }
    }

    protected BroadcastReceiver prepareReceiver(){
        return null;
    }
}

