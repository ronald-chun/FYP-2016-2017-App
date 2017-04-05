package com.cuhk.ieproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private Button login;
    private EditText etID;
    private Check check;
    private Session session;
    private Setting mySetting;

    int uid;
    int pref;
    String uname;
    String utscore;
    String sid;
    String description;
    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        HttpsTrustManager.allowAllSSL();

        check = new Check(this);
        session =new Session(this);
        mySetting = new Setting(this);
        login = (Button)findViewById(R.id.btnLogin);
        login.setOnClickListener(this);
        etID = (EditText)findViewById(R.id.etID);

        if(session.loggedin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                login();
                break;
            default:
        }

    }

    private void login(){
        String id = etID.getText().toString();

//        final UUID uuid = showLoadingDialog();
//        Request request = new User.LoginRequest(LoginActivity.this, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                if (response != null){
//                    Log.e("response", response.toString());
//                    session.setLoggedin(true);
//                    mySetting.setUsername("123");
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
//                }else{
//                    new ErrorDialog.Builder(LoginActivity.this)
//                            .error(new Error("AU001",""))
//                            .show();
//                }
//            }
//        }, new RequestWorker.ErrorListener() {
//            @Override
//            public void onErrorResponse(Error error) {
//                hideLoadingDialog(uuid);
//                new ErrorDialog.Builder(LoginActivity.this)
//                        .error(error)
//                        .show();
//            }
//        });
//        RequestWorker.getInstance(LoginActivity.this).addToRequestQueue(request);

        requestUserInfo postUserID = (requestUserInfo) new requestUserInfo();
        postUserID.execute("user/", id);

//        if (check.verifyUser(id)){
//            session.setLoggedin(true);
//            mySetting.setUsername(id);
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        } else{
//            Toast.makeText(getApplicationContext(), "Wrong id or password", Toast.LENGTH_SHORT).show();
//        }

    }

    private class requestUserInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            final StringBuilder responseOutput = new StringBuilder();
            try {
                String tmp = getString(R.string.api_path) +"/app/api/";
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
                JSONArray josnArr  = jsonObj.getJSONArray("User");
                for (int i = 0; i < josnArr.length(); i++) {
                    JSONObject userInfo = josnArr.getJSONObject(i);
                    uid = userInfo.getInt("uid");
                    pref = userInfo.getInt("pref");
                    uname = userInfo.getString("uname");
                    utscore = userInfo.getString("utscore");
                    sid = userInfo.getString("sid");
                    description = userInfo.getString("description");
                }

                session.setLoggedin(true);
                mySetting.setUID(uid);
                mySetting.setPref(pref);
                mySetting.setUsername(uname);
                mySetting.setUtsore(utscore);
                mySetting.setSID(sid);
                mySetting.setDescription(description);
                Toast.makeText(getApplicationContext(), " Login Succeed ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), " Login Failed ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }

//    public boolean isJSONValid(String test) {
//        try {
//            new JSONObject(test);
//        } catch (JSONException ex) {
//            // edited, to include @Arthur's comment
//            // e.g. in case JSONArray is valid as well...
//            try {
//                new JSONArray(test);
//            } catch (JSONException ex1) {
//                return false;
//            }
//        }
//        return true;
//    }

}

