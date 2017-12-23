package com.example.kalyan.barcodedetect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class lendActivity extends AppCompatActivity {

    TextView isbn_tv,bookname_tv,left_tv;
    String isbn_no ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend);

        isbn_tv = (TextView) findViewById(R.id.isbn_lend);
        bookname_tv = (TextView) findViewById(R.id.book_name_lend);
        left_tv = (TextView) findViewById(R.id.totalLeft);

        if(getIntent() != null){
            isbn_no =getIntent().getStringExtra("data");
            String todisplay = "ISBN no. is:"+isbn_no;
            isbn_tv.setText(todisplay);
        }

       final Button lend = (Button) findViewById(R.id.lend);

        lend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundTask backgroundTask = new BackgroundTask(lendActivity.this);
                String method = "lend";
                backgroundTask.execute(method,isbn_no);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String responce = BackgroundTask.responce;

                String[] sp =  responce.split("%");
                Log.e(sp.toString(),"");
                bookname_tv.setText("BookName: "+sp[0]);
                left_tv.setText("Left: "+sp[1]);

            }
        });
    }

    public void scan(View view) {
        Intent intent = new Intent(this,scannerActivity.class);
        intent.putExtra("activity","lend");
        startActivity(intent);
    }
}
