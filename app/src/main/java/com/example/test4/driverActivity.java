package com.example.test4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class driverActivity extends AppCompatActivity {

    private String userID;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
            userPassword = intent.getStringExtra("userPassword");
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 필요한 권한이 없는 경우 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }

        Button start_drive = (Button) findViewById(R.id.start_dirve);
        start_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent driveIntent = new Intent(driverActivity.this, drivingActivity.class);
                driveIntent.putExtra("userID",userID);
                driverActivity.this.startActivity(driveIntent);
            }
        });

        Button uploadButton = findViewById(R.id.button2);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lostItemUploadIntent = new Intent(driverActivity.this, LostitemUploadActivity.class);
                lostItemUploadIntent.putExtra("userID", userID);
                lostItemUploadIntent.putExtra("userPassword", userPassword);
                startActivity(lostItemUploadIntent);
            }
        });
        
    }


}