package com.example.test4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LocationselectActivity extends AppCompatActivity {

    private Button route1;
    private Button route2;
    private Button route3;
    private Button route4;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        route1 = findViewById(R.id.route1); //도안
        route2 = findViewById(R.id.route2); //세종
        route3 = findViewById(R.id.route3); //계룡
        route4 = findViewById(R.id.route4); //가오동

        route1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(LocationselectActivity.this, BuslocationActivity.class);
                next.putExtra("userID",userID);
                next.putExtra("busID","admin1");
                startActivity(next);
                finish();

            }
        });
        route2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(LocationselectActivity.this, BuslocationActivity.class);
                next.putExtra("userID",userID);
                next.putExtra("busID","admin2");
                startActivity(next);
                finish();
            }
        });
        route3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(LocationselectActivity.this, BuslocationActivity.class);
                next.putExtra("userID",userID);
                next.putExtra("busID","admin3");
                startActivity(next);
                finish();
            }
        });
        route4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(LocationselectActivity.this, BuslocationActivity.class);
                next.putExtra("userID",userID);
                next.putExtra("busID","admin4");
                startActivity(next);
                finish();
            }
        });
    }
}
