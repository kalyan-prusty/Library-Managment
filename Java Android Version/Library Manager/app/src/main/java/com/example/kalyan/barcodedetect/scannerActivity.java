package com.example.kalyan.barcodedetect;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{


    private ZXingScannerView scannerView;

    String activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);

        if(getIntent() != null){
            activity = getIntent().getStringExtra("activity");
        }
        setContentView(scannerView);
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        if(activity.equals("add_a_book")) {
            Intent intent = new Intent(this, Add_a_book_Activity.class);
            intent.putExtra("data", result.getText().toString());
            Toast.makeText(getApplicationContext(), result.getText().toString(), Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }else if(activity.equals("lend")){
            Intent intent = new Intent(this, lendActivity.class);
            intent.putExtra("data", result.getText().toString());
            Toast.makeText(getApplicationContext(), result.getText().toString(), Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }else if(activity.equals("return")){
            Intent intent = new Intent(this, returnActivity.class);
            intent.putExtra("data", result.getText().toString());
            Toast.makeText(getApplicationContext(), result.getText().toString(), Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        scannerView.resumeCameraPreview(this);
        finish();
    }
}
