package com.cuhk.ieproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button login;
    private EditText etID, etPassword;
    private Check check;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        check = new Check(this);
        session =new Session(this);

        login = (Button)findViewById(R.id.btnLogin);
        login.setOnClickListener(this);
        etID = (EditText)findViewById(R.id.etID);
        etPassword = (EditText)findViewById(R.id.etPassword);

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
        String password = etPassword.getText().toString();
        if (check.getUser(id, password)){
            session.setLoggedin(true);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else{
            Toast.makeText(getApplicationContext(), "Wrong id or password", Toast.LENGTH_SHORT).show();
        }
    }
}
