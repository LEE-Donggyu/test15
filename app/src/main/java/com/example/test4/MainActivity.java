package com.example.test4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    String userID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
        }

        Button findButton = findViewById(R.id.findButton);
        Button reservationButton = findViewById(R.id.reservationButton);
        Button ticketButton =findViewById(R.id.ticketButton);
        Button buslocButton = findViewById(R.id.bus_location);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lostitemintent = new Intent(MainActivity.this, LostItemActivity.class);
                lostitemintent.putExtra("userID", userID);
                startActivity(lostitemintent);
            }
        });

        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reservation = new Intent(MainActivity.this, ReservationActivity.class);
                reservation.putExtra("userID", userID);
                startActivity(reservation);
            }
        });

        ticketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ticketintent = new Intent(MainActivity.this, TicketActivity.class);
                ticketintent.putExtra("userID", userID);
                startActivity(ticketintent);
            }
        });

        buslocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent busloc = new Intent(MainActivity.this, LocationselectActivity.class);
                busloc.putExtra("userID",userID);
                startActivity(busloc);
            }
        });
    }
}