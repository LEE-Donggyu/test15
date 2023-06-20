package com.example.test4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class driverActivity extends AppCompatActivity {

    private String userID;
    private TextView route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        route = findViewById(R.id.route_name);

        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
        }

        if(userID.equals("admin1")){
            route.setText("도안");
        } else if (userID.equals("admin2")) {
            route.setText("세종, 노은");
        } else if (userID.equals("admin3")) {
            route.setText("계룡, 진잠, 관저동");
        } else if (userID.equals("admin4")) {
            route.setText("가오동, 판암동");
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
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
                startActivity(lostItemUploadIntent);
            }
        });

        Button finditemupdate = findViewById(R.id.finditemupdate);
        finditemupdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent finditemupdate = new Intent(driverActivity.this, finditemdriver.class);
                finditemupdate.putExtra("userID", userID);
                startActivity(finditemupdate);
            }
        });

        
    }


}