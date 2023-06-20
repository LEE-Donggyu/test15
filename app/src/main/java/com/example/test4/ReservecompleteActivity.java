package com.example.test4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ReservecompleteActivity extends AppCompatActivity {
    private Button home;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");


        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReservecompleteActivity.this, MainActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(ReservecompleteActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("userID", userID);
        startActivity(intent);
        finish();
    }

}
