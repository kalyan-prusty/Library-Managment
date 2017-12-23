package com.example.kalyan.barcodedetect;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    ListView leftlist;
    DrawerLayout mDrawerLayout;
    AutoCompleteTextView auto;
    ArrayList autoArray;
    TextView book_name,isbn_no,total_no,present_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leftlist = (ListView) findViewById(R.id.leftlist);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        auto = (AutoCompleteTextView) findViewById(R.id.auto);
        book_name = (TextView) findViewById(R.id.main_name);
        isbn_no = (TextView) findViewById(R.id.main_number);
        total_no = (TextView) findViewById(R.id.main_total);
        present_no = (TextView) findViewById(R.id.main_present);

        String navigationSt[] = new String[]{"Add a Book          ","Lend a book          ","Return a book          ",
                "Log out         "};
        int navigationImg[] = new int[]{R.drawable.ic_add_a_book,R.drawable.ic_lend,R.drawable.ic_return,R.drawable.ic_log_out};
        MyDrawerAdapter navigationAdapter = new MyDrawerAdapter(getApplicationContext(), navigationSt, navigationImg);
        leftlist.setAdapter(navigationAdapter);

        leftlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectFromDrawer(position);
                mDrawerLayout.closeDrawers();
            }
        });

        autoArray = new ArrayList();
        ArrayPopulate arraypop = new ArrayPopulate();
        arraypop.execute();
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Search search = new Search();
                search.execute(auto.getText().toString());
            }
        });

    }

    public void selectFromDrawer(int position){
        switch (position) {
            case 0:
                Add_a_book();
                break;
            case 1:
                Lend_a_book();
                break;
            case 2:
                Return_a_book();
                break;
            case 3:
                Log_out();
                break;
        }
    }

    private void Log_out() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(BackgroundTaskLogin.USER_NAME,"");
        editor.putString(BackgroundTaskLogin.USER_PASS,"");
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void Add_a_book(){
        Intent intent = new Intent(this,Add_a_book_Activity.class);
        startActivity(intent);
    }

    private void Lend_a_book(){
        Intent intent = new Intent(this,lendActivity.class);
        startActivity(intent);
    }

    private void Return_a_book(){
        Intent intent = new Intent(this,returnActivity.class);
        startActivity(intent);
    }

    public class MyDrawerAdapter extends BaseAdapter {

        private Context context;
        private String[] titles;
        private int[] images;
        private LayoutInflater inflater;

        public MyDrawerAdapter(Context context, String[] titles, int[] images) {
            this.context = context;
            this.titles = titles;
            this.images = images;
            this.inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.singlenavigation_view, null);
                mViewHolder = new ViewHolder();
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            mViewHolder.tvTitle = (TextView) convertView
                    .findViewById(R.id.text_navigation);
            mViewHolder.ivIcon = (ImageView) convertView
                    .findViewById(R.id.image_navigation);

            mViewHolder.tvTitle.setText(titles[position]);
            mViewHolder.ivIcon.setImageResource(images[position]);

            return convertView;
        }

        private class ViewHolder {
            TextView tvTitle;
            ImageView ivIcon;
        }
    }

public class ArrayPopulate extends AsyncTask<Void,Void,String>{

    @Override
    protected String doInBackground(Void... params) {
        String reg_url = "http://192.168.1.2/Library/populate.php";
        URL url = null;
        String responce = "";
        try {
            url = new URL(reg_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            Log.e(connection.getResponseMessage(), "");

            InputStream inputStream = connection.getInputStream();
            responce = readFromStream(inputStream);
            connection.disconnect();

            return responce;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        String[] books = s.split("%");
        for(int i = 0 ;i < books.length ; i++){
            autoArray.add(books[i]);
        }
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,autoArray);
        auto.setAdapter(adapter);

        super.onPostExecute(s);
    }
}

    public class Search extends AsyncTask<String ,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://192.168.1.2/Library/search.php";
            URL url = null;
            String responce = "";
            String bookname = params[0];
            reg_url = reg_url+"?book_name="+bookname;
            Log.e("reg_url",reg_url);
            try {
                url = new URL(reg_url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                Log.e(connection.getResponseMessage(), "");

                InputStream inputStream = connection.getInputStream();
                responce = readFromStream(inputStream);
                connection.disconnect();

                return responce;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
           String[] strings = s.split("%");
            book_name.setText(strings[0]);
            isbn_no.setText(strings[1]);
            total_no.setText(strings[2]);
            present_no.setText(strings[3]);
        }
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
