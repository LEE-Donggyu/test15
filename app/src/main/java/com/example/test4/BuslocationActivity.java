package com.example.test4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuslocationActivity extends AppCompatActivity {

    private Handler handler;
    private GoogleMap googleMap;
    private String busID;
    private String userID;
    private Button checkend;
    private Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_location);

        Intent intent = getIntent();
        busID = intent.getStringExtra("busID");
        userID = intent.getStringExtra("userID");
        checkend = findViewById(R.id.bus_end);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.busmap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                startLocationUpdates();
            }
        });
        checkend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent previous = new Intent(BuslocationActivity.this, MainActivity.class);
                previous.putExtra("userID",userID);
                startActivity(previous);
                finish();
            }
        });
    }

    private void startLocationUpdates() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchAndDisplayLocation();
                handler.postDelayed(this, 1000);
            }
        },0);
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchAndDisplayLocation() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    String server_url = "http://bestknow98.cafe24.com/buslocation.php";
                    URL url = new URL(server_url + "?id=" + busID);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Log.i("tag","msg"+response);
                    reader.close();

                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String response) {
                if (response != null) {
                    try {
                        JSONObject locationObject = new JSONObject(response);
                        JSONArray jsonArray = locationObject.getJSONArray("response");
                        JSONObject object = jsonArray.getJSONObject(0);
                        double lat = object.getDouble("lat");
                        double lon = object.getDouble("lon");
                        Log.i("tag","lat:"+lat+",lon:"+lon);
                        LatLng position = new LatLng(lat, lon);
                        if(marker == null){
                            marker = googleMap.addMarker(new MarkerOptions().position(position));
                            Log.i("tag","lat:"+lat+",lon:"+lon);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                        }else{
                            Log.i("tag","lat:"+lat+",lon:"+lon);
                            marker.setPosition(position);
                        }
//                        googleMap.clear(); // 이전 마커를 제거하고 새로운 위치에 마커 추가
//                        googleMap.addMarker(new MarkerOptions().position(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null); // 핸들러 중지
        }
    }
}