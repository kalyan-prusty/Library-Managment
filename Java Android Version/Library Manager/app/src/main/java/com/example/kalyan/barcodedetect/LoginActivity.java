package com.example.kalyan.barcodedetect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView intenttv;
    EditText username_et,pass_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intenttv = (TextView) findViewById(R.id.register_tv);
        username_et = (EditText) findViewById(R.id.user_name);
        pass_et = (EditText) findViewById(R.id.pass);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getString(BackgroundTaskLogin.USER_NAME,"").equals("")){
            username_et.setText(prefs.getString(BackgroundTaskLogin.USER_NAME,""));
            pass_et.setText(prefs.getString(BackgroundTaskLogin.USER_PASS,""));
        }

        intenttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,regActivity.class);
                startActivity(intent);
                username_et.setText("");
                pass_et.setText("");
            }
        });
    }

    public void Login(View view) {

        String username = username_et.getText().toString();
        String pass = pass_et.getText().toString();
        String gender = null;

        String method = "login";
        BackgroundTaskLogin backgroundTaskLogin = new BackgroundTaskLogin(this);
        backgroundTaskLogin.execute(method,username,pass,gender);
    }
}
