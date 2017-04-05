package com.cuhk.ieproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cuhk.ieproject.model.Card;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Felix on 2017-04-01.
 */

public class SettingActivity extends AppCompatActivity {
    Session session;
    TextView user_name;
    String username;
    String response;
    Button btnLogout, okButton, timer;
    Switch cameraSwitch, contextSwitch;
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
        timer = (Button)findViewById(R.id.timerBtn);
        okButton = (Button)findViewById(R.id.okButton);
        cameraSwitch = (Switch)findViewById(R.id.cameraSwitch);
        contextSwitch = (Switch)findViewById(R.id.contextSwitch);
        if(mySetting.camera()){
            cameraSwitch.setChecked(true);
        }else{
            cameraSwitch.setChecked(false);
        }
        if(mySetting.context()){
            contextSwitch.setChecked(true);
        }else{
            contextSwitch.setChecked(false);
        }
        cardSizeVal = (TextView)findViewById(R.id.card_size_value);
        cardSizeSeekbar = (SeekBar)findViewById(R.id.card_size_seekbar);
        cardSizeSeekbar.setMax(9);
        cardSizeSeekbar.setProgress(mySetting.getCardSize());
        cardSizeVal.setText(cardSizeSeekbar.getProgress()+"/"+cardSizeSeekbar.getMax());

    }

    public void setEventListener(){
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                logout();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                finish();
            }
        });

        timer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                requestStartTimer postTimer = (requestStartTimer) new requestStartTimer();
                postTimer.execute("test/", String.valueOf(mySetting.getUID()));

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

        contextSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mySetting.setEnableContext(true);
                } else {
                    mySetting.setEnableContext(false);
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


    private class requestStartTimer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            final StringBuilder responseOutput = new StringBuilder();
            try {
                String tmp = "https://192.168.65.99:8080/app/api/";
                for (String p: params) {
                    tmp += p;
                }
                Log.e("tmp", tmp);
//                URL url = new URL("https://192.168.65.99:8080/app/api/" + params[0]);
                URL url = new URL(tmp);

                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                String urlParameters = "";
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
//                response = String.valueOf(responseOutput);
//                Log.d("xxxxx", String.valueOf(output));

//                MainActivity.this.runOnUiThread(new Runnable() {
//
//
//                    @Override
//                    public void run() {
////                        outputView.setText(output);
//
//                    }
//                });
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return responseOutput.toString();
        }

        public void onPostExecute(String result) {
            response = result;
            try {
                JSONObject jsonObj = new JSONObject(response);
                String status  = jsonObj.getString("status");
                if(status.equals("Sucess")){
                    Toast.makeText(SettingActivity.this, "Timer Start", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

}
