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
import java.util.List;

public class LostItemActivity extends AppCompatActivity {

    private String userID = null;

    private ListView findListView;
    private FindItemAdapter adapter;
    private List<FindItem> findList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item);

        findListView = findViewById(R.id.findlistView);
        findList = new ArrayList<>();
        adapter = new FindItemAdapter(getApplicationContext(), findList);
        findListView.setAdapter(adapter);


        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
        }

        new BackgroundTask().execute();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute(){
            target = "http://bestknow98.cafe24.com/FindItem.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine())!=null){
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
                String userName, LostDate, LostItemName, LostItemPicture, id;
                while(count < jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    userName = object.getString("userName");
                    LostDate = object.getString("LostDate");
                    LostItemName = object.getString("LostItemName");
                    LostItemPicture = object.getString("LostItemPicture");

                    FindItem findItem = new FindItem(LostItemName,  LostDate, userName, LostItemPicture);
                    findList.add(findItem);
                    count++;
                }
                adapter.notifyDataSetChanged();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
