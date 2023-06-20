package com.example.test4;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ReservecheckActivity extends AppCompatActivity {

    private TextView data;
    private Button complete;
    private Runnable sendDataRunnable;

    private String userName;
    private String date;
    private String place;
    private String route;
    private int turn;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservedata_check);
        String page = "http://bestknow98.cafe24.com/complete.php";
        String count_page = "http://bestknow98.cafe24.com/reservecount.php";
        String load_name = "http://bestknow98.cafe24.com/Getusername.php";
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        date = intent.getStringExtra("date");
        place = intent.getStringExtra("place");
        route = intent.getStringExtra("route");
        turn  = intent.getIntExtra("turn",0);
        String payload = "userID="+userID;

        new HttpRequestTask().execute(load_name,payload);


        data = findViewById(R.id.reserve_data);


        complete = findViewById(R.id.reserve_complete);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    String data = "date="+date+"&place="+place+"&turn="+turn+"&route="+route+"&userID="+userID;
                    @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, Boolean> sendDataTask = new AsyncTask<String, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(String... params) {
                            try{
                                String data = params[0];
                                URL url = new URL(count_page + "?date=" + date + "&routename=" + route);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                StringBuilder response = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }
                                reader.close();
                                Log.i("tag","response: "+response);
                                JSONObject json = new JSONObject(response.toString());
                                JSONArray responseArray = json.getJSONArray("response");
                                Log.i("tag","예약갯수: "+responseArray.length());
                                if(responseArray.length() >= 45){
                                    return false;
                                } else{
                                    URL data_url = new URL(page);
                                    HttpURLConnection connection = (HttpURLConnection) data_url.openConnection();
                                    Log.i("tag","conn 연결");
                                    connection.setRequestProperty("Accept","application/json");
                                    connection.setRequestMethod("POST");
                                    connection.setDoOutput(true);
                                    OutputStream outputStream = connection.getOutputStream();
                                    connection.getOutputStream().write(data.getBytes("utf-8"));
                                    if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                        Log.i("tag","접속");
                                    }
                                    outputStream.flush();
                                    outputStream.close();
                                    connection.disconnect();
                                    return true;
                                }
                            } catch (Exception e){
                                Log.i("tag","error: "+e);
                                return false;
                            }
                        }
                        @Override
                        protected void onPostExecute(Boolean result){
                            if(result){
                                Intent next = new Intent(ReservecheckActivity.this, ReservecompleteActivity.class);
                                next.putExtra("userID", userID);
                                startActivity(next);
                                finishAffinity();
                            } else{
                                showAlertDialog("알림","죄송합니다, 인원이 다 찼습니다");
                            }
                        }
                    };

                    sendDataTask.execute(data);
                }
            }
        });



    }
    private class HttpRequestTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params){
            String requestUrl = params[0];
            String payload = params[1];

            try{
                URL url = new URL(requestUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setDoOutput(true);

                //요청데이터를 서버로 전송
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(payload.getBytes());
                outputStream.flush();
                outputStream.close();

                // 응답 받기
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK){
                    Log.i("tag","서버연결 완료");
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line = reader.readLine();;
                    response.append(line);
                    Log.i("tag","데이터:"+response);
                    reader.close();
                    return response.toString();
                } else{
                    throw new IOException("HTTP reqest failed");
                }
            } catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String response){
            if(response != null){
                userName = response;
                data.setText("예약 날짜: "+date+"\n"+"선택 장소: "+place+"\n"+"회차: "+turn+"\n"+"선택 노선:"+route+"\n"+
                        "이름:"+userName+"\n");
            } else{
                Log.i("tag","에러");
            }
        }
    }
    private void showAlertDialog(String title, String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("tag","알림 다이얼로그 표시");
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservecheckActivity.this);
                builder.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();


            }
        });
    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
