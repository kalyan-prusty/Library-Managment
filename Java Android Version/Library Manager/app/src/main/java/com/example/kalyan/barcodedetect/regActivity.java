package com.example.kalyan.barcodedetect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class regActivity extends AppCompatActivity {

    EditText username_et,pass_et;
    RadioButton malebt,femalebt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        username_et = (EditText) findViewById(R.id.user_name_reg);
        pass_et = (EditText) findViewById(R.id.pas_reg);

        malebt = (RadioButton)findViewById(R.id.maleBt);
        femalebt = (RadioButton)findViewById(R.id.femaleBt);

    }

    public void register(View view) {
        if ((malebt.isChecked() || femalebt.isChecked()) && !(username_et.getText().toString().trim()).equals("")
                &&!(pass_et.getText().toString().trim()).equals("")) {
            String username = username_et.getText().toString();
            String pass = pass_et.getText().toString();
            String gender = null;

            if (malebt.isChecked()) {
                gender = "M";
            } else if(femalebt.isChecked()) {
                gender = "F";
            }

            String method = "register";
            BackgroundTaskLogin backgroundTaskLogin = new BackgroundTaskLogin(this);
            backgroundTaskLogin.execute(method,username,pass,gender);
            username_et.setText("");
            pass_et.setText("");
        }
        else {
            Toast.makeText(regActivity.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
        }
    }


}
