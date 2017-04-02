package com.cuhk.ieproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Felix on 2017-04-01.
 */

public class SettingActivity extends AppCompatActivity {
    Session session;
    TextView user_name;
    String username;
    Button btnLogout;
    Switch cameraSwitch;
    TextView cardSizeVal;
    SeekBar cardSizeSeekbar;
    int cardSizeSeekProgress;
    private Setting mySetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mySetting = new Setting(this);
        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }
        initComponent();
        setEventListener();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        SettingActivity.this.finish();
    }

    public void initComponent(){
        user_name = (TextView)findViewById(R.id.user_name_val);
        user_name.setText(mySetting.getUsername());
        btnLogout = (Button)findViewById(R.id.btnLogout);
        cameraSwitch = (Switch)findViewById(R.id.cameraSwitch);
        if(mySetting.camera()){
            cameraSwitch.setChecked(true);
        }else{
            cameraSwitch.setChecked(false);
        }
        cardSizeVal = (TextView)findViewById(R.id.card_size_value);
        cardSizeSeekbar = (SeekBar)findViewById(R.id.card_size_seekbar);
        cardSizeSeekbar.setMax(9);
        cardSizeSeekbar.setProgress(4);
        cardSizeVal.setText(cardSizeSeekbar.getProgress()+"/"+cardSizeSeekbar.getMax());

    }

    public void setEventListener(){
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                logout();
            }
        });

        cameraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    mySetting.setEnableCamera(true);
                } else {
                    mySetting.setEnableCamera(false);
                }
            }
        });

        cardSizeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    if(progress==0){
                        cardSizeSeekProgress = progress + 1;
                    } else cardSizeSeekProgress = progress;
                    cardSizeVal.setText(cardSizeSeekProgress+"/"+cardSizeSeekbar.getMax());
                    mySetting.setCardSize(cardSizeSeekProgress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                cardSizeVal.setText(cardSizeSeekProgress+"/"+cardSizeSeekbar.getMax());
//                mySetting.setCardSize(cardSizeSeekProgress);
            }
        });
    }

    private void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
    }

}
