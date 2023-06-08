package com.example.test4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class ReservecheckActivity extends AppCompatActivity {

    private TextView data;
    private Button complete;
    private Runnable sendDataRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservedata_check);
        String page = "http://bestknow98.cafe24.com/complete.php";
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String date = intent.getStringExtra("date");
        String place = intent.getStringExtra("place");
        String route = intent.getStringExtra("route");
        int turn  = intent.getIntExtra("turn",0);
        long now = System.currentTimeMillis();
        Date current = new Date(now);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = dateFormat.format(current);
        data = findViewById(R.id.reserve_data);
        data.setText("버스예약날짜: "+date+"\n"+"선택하신장소: "+place+"\n"+"회차: "+turn+"\n"+"선택하신 노선:"+route+"\n"+
                "유저아이디:"+userID+"\n"+"예약한 날짜:"+getTime);

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
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
