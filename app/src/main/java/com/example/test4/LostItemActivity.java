package com.example.test4;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class LostItemActivity extends AppCompatActivity {

    String userID = null;
    String userPassword = null;

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

        findList.add(new FindItem("공지사항", "개발자1", "2023-05-31"));

        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
            userPassword = intent.getStringExtra("userPassword");
        }

        new BackgroundTask().execute();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target = "http://bestknow98.cafe24.com/FindItem.php";

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp).append("\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();
            } catch (Exception e) {
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
            try {
                Log.d("tag",result);
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String findItem, findName, findDate;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    findItem = object.getString("findItem");
                    findName = object.getString("findName");
                    findDate = object.getString("findDate");
                    FindItem finditem = new FindItem(findItem, findName, findDate);
                    findList.add(finditem);
                    count++;
                }
                adapter.notifyDataSetChanged(); // 어댑터에 데이터 변경을 알림
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
