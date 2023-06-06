package com.example.test4;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.List;

public class TicketActivity extends AppCompatActivity {

    private ListView ticketListView;
    private TicketListAdapter adapter;
    private List<Ticket> ticketList;

    private String userID;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
            userPassword = intent.getStringExtra("userPassword");
        }

        ticketListView = findViewById(R.id.reservationList);
        ticketList = new ArrayList<Ticket>();
        adapter = new TicketListAdapter(getApplicationContext(), ticketList);
        ticketListView.setAdapter(adapter);

        new BackgroundTask().execute();

    }

    class BackgroundTask extends AsyncTask<Void, Void, String>{

        String target;

        @Override
        protected void onPreExecute(){
            target = "http://bestknow98.cafe24.com/TicketList.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target + "?userID=" + userID);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null){
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void...values){
            super.onProgressUpdate();
        }
        @Override
        public void onPostExecute(String result){
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String routename, date, userName, pickupname, turn;
                while(count < jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    routename = object.getString("routename");
                    date = object.getString("date");
                    userName = object.getString("userName");
                    pickupname = object.getString("pickupname");
                    turn = object.getString("turn");
                    Ticket ticket = new Ticket(routename, date, userName, pickupname, turn);
                    ticketList.add(ticket);
                    count++;
                }
                Collections.reverse(ticketList);
                adapter.notifyDataSetChanged();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}