package com.cuhk.ieproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private Button login;
    private EditText etID;
    private Check check;
    private Session session;
    private Setting mySetting;
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

        if (check.verifyUser(id)){
            session.setLoggedin(true);
            mySetting.setUsername(id);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else{
            Toast.makeText(getApplicationContext(), "Wrong id or password", Toast.LENGTH_SHORT).show();
        }
    }
}
