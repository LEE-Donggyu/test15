package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class finditemdriver extends AppCompatActivity {

    private String userID = null;

    private ListView findListupdateView;
    private UpdateItemAdapter adapter;
    private SwipeRefreshLayout mysrl;
    private List<UpdateItem> updateList;

    private int currentPage = 0;

    private String id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finditemdriver);

        findListupdateView = findViewById(R.id.findlistupdateView);
        updateList = new ArrayList<>();
        adapter = new UpdateItemAdapter(getApplicationContext(), updateList);
        findListupdateView.setAdapter(adapter);

        mysrl = findViewById(R.id.content_srl);
        mysrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                updateList.clear();
                new BackgroundTask().execute(currentPage);
                mysrl.setRefreshing(false);
            }
        });

        findListupdateView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    currentPage++; // Load the next page
                    new BackgroundTask().execute(currentPage);
                }
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
        }

        new BackgroundTask().execute(currentPage);
    }

    class BackgroundTask extends AsyncTask<Integer, Void, String> {

        private static final int ITEMS_PER_PAGE = 10;
        private int currentPage = 0;

        String target;

        @Override
        protected void onPreExecute(){
            target = "http://bestknow98.cafe24.com/FindItem.php";
        }

        @Override
        protected String doInBackground(Integer... params) {
            int page = params[0];
            currentPage = page;

            int startIndex = currentPage * ITEMS_PER_PAGE;
            int endIndex = startIndex + ITEMS_PER_PAGE;

            try{
                URL url = new URL(target + "?start=" + startIndex + "&end=" + endIndex);
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
        public void onProgressUpdate(Void... values){
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
                    id = object.getString("id");
                    UpdateItem updateItem = new UpdateItem(id, LostItemName,  LostDate, userName, LostItemPicture);
                    updateList.add(updateItem);
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