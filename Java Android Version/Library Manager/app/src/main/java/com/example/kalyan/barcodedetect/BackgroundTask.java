package com.example.kalyan.barcodedetect;

import android.content.Context;
import android.os.AsyncTask;
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
 * Created by KALYAN on 07-12-2017.
 */

public class BackgroundTask extends AsyncTask<String,Void,String> {

    Context context;
    static String responce = null;
    BackgroundTask(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String method = params[0];
        if(method.equals("add_a_book")) {
            String reg_url = "http://192.168.1.2/Library/Add_a_book.php";
            String bookname = params[1];
            String bookid = params[3];
            String no_of_pieces = params[2];
            reg_url = reg_url + "?bookname=" + bookname +"&bookid="+bookid+"&numberofpieces="+no_of_pieces;
            Log.e("reg_url",reg_url);
            URL url = null;
            try {
                url = new URL(reg_url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                Log.e(connection.getResponseMessage(), "");
                connection.disconnect();
                return "Book Updated.....";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        else if(method.equals("lend")){
            String reg_url = "http://192.168.1.2/Library/lend.php";
            String bookid = params[1];
            reg_url = reg_url + "?isbn="+bookid;
            Log.e("reg_url",reg_url);
            URL url = null;
            try {
                url = new URL(reg_url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                Log.e(connection.getResponseMessage(), "");

                InputStream inputStream = connection.getInputStream();
                responce = readFromStream(inputStream);

                connection.disconnect();
                if(!responce.contains("N/A")) {
                    return "Book lending Updated.....";
                }else{
                    return responce;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        else if(method.equals("return")){
            String reg_url = "http://192.168.1.2/Library/return.php";
            String bookid = params[1];
            reg_url = reg_url + "?isbn="+bookid;
            Log.e("reg_url",reg_url);
            URL url = null;
            try {
                url = new URL(reg_url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                Log.e(connection.getResponseMessage(), "");

                InputStream inputStream = connection.getInputStream();
                responce = readFromStream(inputStream);

                connection.disconnect();
                if(!responce.contains("N/A")) {
                    return "Book returned.....";
                }else{
                    return responce;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }


    @Override
    protected void onPostExecute(String  result) {
        if (result.contains("N/A")) {
            Toast.makeText(context, "Book not found in library", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
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
