package com.example.test4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class drivingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Runnable sendDataRunnable;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_driving);
        String page = "http://bestknow98.cafe24.com/location_1.php";
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }

        Button end_drive = (Button) findViewById(R.id.end_drive);
        end_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent end = new Intent(drivingActivity.this, driverActivity.class);
                end.putExtra("userID", userID);
                finishAffinity();
                drivingActivity.this.startActivity(end);
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (mMap != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    sendDataRunnable = new Runnable() {
                        @Override
                        public void run() {
                            String params = "param=" + location.getLatitude() + "&param2=" + location.getLongitude() + "&id=" + userID;
                            try {
                                URL url = new URL(page);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                if (conn != null) {
                                    conn.setRequestProperty("Accept", "application/json");
                                    conn.setRequestMethod("POST");
                                    conn.setDoOutput(true);
                                    OutputStream outputStream = conn.getOutputStream();
                                    conn.getOutputStream().write(params.getBytes("utf-8"));
                                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                        Log.i("tag", "접속");
                                    }
                                    outputStream.flush();
                                    outputStream.close();
                                    conn.disconnect();
                                }
                            } catch (Exception e) {
                                Log.i("tag", "error: " + e);
                            }
                        }
                    };
                    Thread thread = new Thread(sendDataRunnable);
                    thread.start();
                    LatLng currentLocation = new LatLng(latitude, longitude);
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("현재 위치"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                }
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}
