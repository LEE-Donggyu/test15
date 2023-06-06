package com.example.test4;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class pickplaceActivity extends AppCompatActivity {
    private ListView ReservationListView;
    private ReservationAdapter reservationAdapter;
    private List<ReservationItem> reservationList;
    private Button place_select;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickupplace);
        Intent intent = getIntent();
        String date = intent.getStringExtra("select_date"); //선택날짜
        String route = intent.getStringExtra("select_route"); //선택노선
        String userID = intent.getStringExtra("userID"); //유저아이디
        int turn = intent.getIntExtra("turn", 0); //선택회차
        ReservationListView = findViewById(R.id.placeviewlist);
        reservationList = new ArrayList<>();
        reservationAdapter = new ReservationAdapter(pickplaceActivity.this, reservationList);
        ReservationListView.setAdapter(reservationAdapter);
        reservationList.clear();
        int routeID;
        if(route.equals("도안")){ //앞에 선택한노선이 도안이면 routeID에 1을주어 도안과 연관된 정류장을 DB에서 가져옴
            routeID = 1;
        } else if (route.equals("계룡")) {
            routeID = 2;
        }else{
            routeID = 0;
        }

        ReservationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String select_place;
                ReservationItem selectedItem = (ReservationItem) reservationAdapter.getItem(position);
                select_place = selectedItem.getStart(); //선택한리스트의 정류장값이 select_place에 들어감

                Intent next = new Intent(pickplaceActivity.this, ReservecheckActivity.class);
                next.putExtra("date",date);
                next.putExtra("place",select_place);
                next.putExtra("route",route);
                next.putExtra("turn",turn);
                next.putExtra("userID",userID);
                startActivity(next);
            }
        });


        new BackgroundTask(routeID).execute();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target = "https://stone-cjioq.run.goorm.site/stone/pickplace.php";
        int routeID;

        public BackgroundTask(int routeID){
            this.routeID = routeID;
        }
        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target + "?routeID=" + routeID);
                Log.i("tag",""+url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Log.i("tag",""+httpURLConnection.getResponseCode());
                String firstLine = bufferedReader.readLine();
                Log.i("tag",firstLine);
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(firstLine).append("\n");
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp).append("\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result) {
            if (result != null){
                try{
                    Log.i("tag",result);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;
                    String bus_start, bus_time;
                    reservationAdapter.notifyDataSetChanged();
                    while(count < jsonArray.length()){
                        JSONObject object = jsonArray.getJSONObject(count);
                        bus_start = object.getString("pickupname");
                        ReservationItem reservationItem = new ReservationItem(bus_start);
                        reservationList.add(reservationItem);
                        count++;
                    }
                    reservationAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                Log.d("tag","reuslt 값 없음");
            }
        }
    }
}