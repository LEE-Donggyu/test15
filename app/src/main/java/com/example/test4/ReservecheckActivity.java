package com.example.test4;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        String load_name = "https://stone-cjioq.run.goorm.io/stone/Getusername.php";
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
                //날짜 픽업장소 회차 노선이름 유저아이디
                {
                    String data = "date="+date+"&place="+place+"&turn="+turn+"&route="+route+"&userID="+userID;
                    sendDataRunnable = new Runnable() {
                        @Override
                        public void run() {
                            try{
                                URL url = new URL(page);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                if(conn != null){
                                    Log.i("tag","conn 연결");
                                    conn.setRequestProperty("Accept","application/json");
                                    conn.setRequestMethod("POST");
                                    conn.setDoOutput(true);
                                    OutputStream outputStream = conn.getOutputStream();
                                    conn.getOutputStream().write(data.getBytes("utf-8"));
                                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                                        Log.i("tag","접속");
                                    }
                                    outputStream.flush();
                                    outputStream.close();
                                    conn.disconnect();
                                }
                            }catch (Exception e){
                                Log.i("tag","error :"+e);
                            }
                        }
                    };
                    Thread thread = new Thread(sendDataRunnable);
                    thread.start();
                }
                Intent next = new Intent(ReservecheckActivity.this, ReservecompleteActivity.class);
                next.putExtra("userID",userID);
                startActivity(next);
                finishAffinity();
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
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
