package com.example.kalyan.barcodedetect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
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

/**
 * Created by KALYAN on 06-12-2017.
 */

public class BackgroundTaskLogin extends AsyncTask<String,Void,String> {
    Context context;

    static final String USER_NAME = "user_name";
    static final String USER_PASS = "password";
    static String responce = null;

    BackgroundTaskLogin(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String method = params[0];
        String reg_url = "http://192.168.1.2/Library/register.php";
        String log_url = "http://192.168.1.2/Library/login.php";

        if(method.equals("register")){
            String username = params[1];
            String pass = params[2];
            String gender = params[3];

            try {
                reg_url = reg_url+"?username="+username+"&password="+pass+"&gender="+gender;
                URL url = new URL(reg_url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                Log.e(connection.getResponseMessage(),"");
                //Toast.makeText(context,connection.getResponseCode()+"",Toast.LENGTH_SHORT).show();
                connection.disconnect();
                return "Registered sucessfully..";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("login")){
            InputStream inputStream = null;
            String username = params[1];
            String pass = params[2];
            String gender = params[3];

            log_url = log_url+"?username="+username+"&password="+pass;
            URL url = null;
            try {
                url = new URL(log_url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                Log.e(urlConnection.getResponseMessage(),"");

                inputStream = urlConnection.getInputStream();
                 responce = readFromStream(inputStream);

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(USER_NAME,username);
                editor.putString(USER_PASS,pass);
                editor.apply();

                return responce;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return "There is some error in Logging In...";
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String  result) {
        Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
        if(result.toLowerCase().contains("welcome")){
            Intent intent = new Intent(context,MainActivity.class);
            context.startActivity(intent);
        }else if(result.toLowerCase().contains("registered")){
            Intent intent = new Intent(context,LoginActivity.class);
            context.startActivity(intent);
        }
    }
}
