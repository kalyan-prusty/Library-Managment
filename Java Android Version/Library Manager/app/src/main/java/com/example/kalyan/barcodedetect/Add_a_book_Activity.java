package com.example.kalyan.barcodedetect;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.Manifest.permission.CAMERA;

public class Add_a_book_Activity extends AppCompatActivity {

    private static final int RequestCode = 1;
    EditText book_name_et,no_of_book_et;
    TextView isbn_tv;
    String isbn_no ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_book_);

        book_name_et = (EditText) findViewById(R.id.book_name);
        no_of_book_et = (EditText) findViewById(R.id.number_of_book);
        isbn_tv = (TextView) findViewById(R.id.result);

        if(getIntent() != null){
            isbn_no =getIntent().getStringExtra("data");
            String todisplay = "ISBN no. is:"+isbn_no;
            isbn_tv.setText(todisplay);
        }

        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String book_name = book_name_et.getText().toString();
                String no_of_books = no_of_book_et.getText().toString();
                upload(book_name,no_of_books,isbn_no);
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scan();
                    }
                });
            }else{
                requestPermission();
            }
        }else{
            ((Button)findViewById(R.id.scan)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scan();
                }
            });
        }
    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(Add_a_book_Activity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},RequestCode);
    }

    public void scan() {
        Intent intent = new Intent(this,scannerActivity.class);
        intent.putExtra("activity","add_a_book");
        startActivity(intent);
    }

    private void upload(String book_name,String no_of_pieces,String book_id) {
        BackgroundTask backgroundtask = new BackgroundTask(getApplicationContext());
        String method = "add_a_book";
        backgroundtask.execute(method,book_name,no_of_pieces,book_id);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
