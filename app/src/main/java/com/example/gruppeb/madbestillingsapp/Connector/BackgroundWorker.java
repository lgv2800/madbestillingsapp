package com.example.gruppeb.madbestillingsapp.Connector;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker extends AsyncTask<String, Void, String> {

    Context mContext;
    AlertDialog statusAlertDialog;
    public BackgroundWorker(Context backgroundWorkerContext) {
        mContext = backgroundWorkerContext;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        //String login_url = "http://localhost:8888/Projekter/Madbestillingsapp%20-%20Webportal/userLogin.php";
        String login_url = "http://192.168.0.129:8888/Projekter/Madbestillingsapp%20-%20Webportal/userLogin.php";

        if (type.equals("login")) {
            try {
                String roomNumber = params[1];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String postData = URLEncoder.encode("roomNumber", "UTF-8") + "=" + URLEncoder.encode(roomNumber, "UTF-8");
                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        statusAlertDialog = new AlertDialog.Builder(mContext).create();
        statusAlertDialog.setTitle("Login status");
    }

    @Override
    protected void onPostExecute(String result) {
        statusAlertDialog.setMessage(result);
        statusAlertDialog.show();
        System.out.println(result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
