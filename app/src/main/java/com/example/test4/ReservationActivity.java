package com.example.test4;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReservationActivity extends AppCompatActivity {
    String userID;
    private Button select_route; //노선선택버튼
    private Button select_date; //날짜선택버튼
    private Button view_btn; //조회 버튼
    private Calendar calendar;
    private Button first_reserve; //1회차 선택버튼
    private Button second_reserve; //2회차 선택버튼
    String route_item=""; //노선선택 저장 변수
    private LinearLayout turn_select;
    private LinearLayout first_turn;
    private LinearLayout second_turn;
    private TextView first_count;
    private TextView second_count;
    private Runnable sendDataRunnable;
    private Runnable GetTicket;
    private String server_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        select_route = findViewById(R.id.route_select);
        server_url = "http://bestknow98.cafe24.com/reservecount.php";

        select_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                builder.setTitle("노선 선택");

                final String[] routes = {"도안","세종, 노은","계룡, 진잠, 관저동","가오동, 판암동"};
                builder.setItems(routes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String selectedroute = routes[i];
                        select_route.setText(selectedroute);
                        route_item = selectedroute;
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        select_date = findViewById(R.id.date_select);

        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        view_btn = findViewById(R.id.route_check);

        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (route_item.isEmpty()) {
                    showAlertDialog("알림", "노선을 선택해주세요.");
                    return;
                }
                if (calendar == null) {
                    showAlertDialog("알림", "날짜를 선택해주세요.");
                    return;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String date = dateFormat.format(calendar.getTime());
                first_count = findViewById(R.id.firstcount);
                second_count = findViewById(R.id.secondcount);
                turn_select = findViewById(R.id.turn_select);
                first_turn = findViewById(R.id.first_turn);
                second_turn = findViewById(R.id.second_turn);
                GetTicket = new Runnable() {
                    @Override
                    public void run() {
                        String server  = "http://bestknow98.cafe24.com/Getreservedata.php";
                        try{
                            URL url = new URL(server + "?date=" + date + "&userID=" + userID);
                            Log.i("tag","url: "+url);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            reader.close();
                            Log.i("tag","response: "+response);
                            JSONObject json = new JSONObject(response.toString());
                            JSONArray responseArray = json.getJSONArray("response");
                            if(responseArray.length() >= 1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("tag","예약된게 이미 있습니다.");
                                        showAlertDialog("알림","선택한 날짜에 이미 예약된게 있습니다.");
                                        turn_select.setVisibility(View.GONE);
                                    }
                                });
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (route_item.equals("세종, 노은")) {
                                            turn_select.setVisibility(View.VISIBLE);
                                            first_turn.setVisibility(View.VISIBLE);
                                            second_turn.setVisibility(View.GONE);
                                        } else {
                                            turn_select.setVisibility(View.VISIBLE);
                                            first_turn.setVisibility(View.VISIBLE);
                                            second_turn.setVisibility(View.VISIBLE);
                                        }

                                    }
                                });

                            }
                        } catch (Exception e){
                            Log.i("tag","error:"+e);
                        }
                    }
                };


                sendDataRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(server_url + "?date=" + date + "&routename=" + route_item);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            reader.close();

                            JSONObject json = new JSONObject(response.toString());
                            JSONArray responseArray = json.getJSONArray("response");

                            int onecount = 0;
                            int twocount = 0;
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject item = responseArray.getJSONObject(i);
                                String turn = item.getString("turn");
                                if (turn.equals("1")) {
                                    onecount++;
                                } else if (turn.equals("2")) {
                                    twocount++;
                                }
                            }

                            int finalOnecount = 45 - onecount;
                            int finalTwocount = 45 - twocount;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    first_count.setText(String.valueOf(finalOnecount));
                                    second_count.setText(String.valueOf(finalTwocount));
                                    if(finalOnecount == 0){
                                        first_reserve.setEnabled(false);
                                        first_reserve.setText("X");
                                    } else{
                                        first_reserve.setEnabled(true);
                                        first_reserve.setText("선택");
                                    }
                                    if(finalTwocount == 0){
                                        second_reserve.setEnabled(false);
                                        second_reserve.setText("X");
                                    }
                                    else{
                                        second_reserve.setEnabled(true);
                                        second_reserve.setText("선택");
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                Thread thread = new Thread(GetTicket);
                thread.start();

                Thread th = new Thread(sendDataRunnable);
                th.start();
            }
        });

        first_reserve = findViewById(R.id.first_reserve);
        second_reserve = findViewById(R.id.second_reserve);

        first_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calendar == null || route_item.isEmpty()) {
                    showAlertDialog("알림", "날짜와 노선을 선택해주세요.");
                    return;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String date = dateFormat.format(calendar.getTime());
                int turn = 1; //회차

                Intent next = new Intent(ReservationActivity.this, pickplaceActivity.class);
                next.putExtra("userID", userID);
                next.putExtra("turn", turn);
                next.putExtra("select_route", route_item);
                next.putExtra("select_date", date);
                startActivity(next);
            }
        });

        second_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calendar == null || route_item.isEmpty()) {
                    showAlertDialog("알림", "날짜와 노선을 선택해주세요.");
                    return;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String date = dateFormat.format(calendar.getTime());
                int turn = 2; //회차

                Intent next = new Intent(ReservationActivity.this, pickplaceActivity.class);
                next.putExtra("userID", userID);
                next.putExtra("turn", turn);
                next.putExtra("select_route", route_item);
                next.putExtra("select_date", date);
                startActivity(next);
            }
        });
    }

    private void showDatePicker() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar dateLimit = Calendar.getInstance();

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        int dayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.WEDNESDAY) {
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
            dateLimit.add(Calendar.DAY_OF_MONTH, 2);
        } else if (dayOfWeek == Calendar.THURSDAY) {
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
            dateLimit.add(Calendar.DAY_OF_MONTH, 1);
        } else if (dayOfWeek == Calendar.FRIDAY) {
            currentDate.add(Calendar.DAY_OF_MONTH, 3);
            dateLimit.add(Calendar.DAY_OF_MONTH, 3);
        } else if (dayOfWeek == Calendar.SATURDAY) {
            currentDate.add(Calendar.DAY_OF_MONTH, 2);
            dateLimit.add(Calendar.DAY_OF_MONTH, 3);
        } else {
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
            dateLimit.add(Calendar.DAY_OF_MONTH, 3);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                updateSelectedDateButton();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(dateLimit.getTimeInMillis());
        datePickerDialog.show();
    }


    private void updateSelectedDateButton() {
        if (calendar != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String selectedDateStr = dateFormat.format(calendar.getTime());
            select_date.setText(selectedDateStr);
        }
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
