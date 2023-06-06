package com.example.test4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.app.DatePickerDialog;
import android.widget.RelativeLayout;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReservationActivity extends AppCompatActivity {
    private ListView ReservationListView; //findListview
    private ReservationAdapter reservationAdapter;
    private List<ReservationItem> reservationList; //findlist
    String userID;
    private Button select_route; //노선선택버튼
    private Button select_date; //날짜선택버튼
    private Button view_btn; //조회 버튼
    private Calendar calendar;
    private Button first_reserve; //1회차 선택버튼
    private Button second_reserve; //2회차 선택버튼
    String route_item=""; //노선선택 저장 변수
    private LinearLayout turn_select;
    private RelativeLayout first_turn;
    private RelativeLayout second_turn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        select_route = findViewById(R.id.route_select);
        select_route.setOnClickListener(new View.OnClickListener() { //노선선택 버튼 코드
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                builder.setTitle("노선 선택");

                final String[] routes = {"세종","도안","계룡","가오동"};
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
        select_date.setOnClickListener(new View.OnClickListener() { //날짜선택 코드
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        view_btn = findViewById(R.id.route_check);
        view_btn.setOnClickListener(new View.OnClickListener() { //조회버튼 코드
            @Override
            public void onClick(View view) {
                turn_select = findViewById(R.id.turn_select);
                first_turn = findViewById(R.id.first_turn);
                second_turn = findViewById(R.id.second_turn);
                if(route_item.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                    builder.setTitle("알림").setMessage("노선을 선택해주세요").setPositiveButton("확인",null).show();
                } else if (calendar == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                    builder.setTitle("알림").setTitle("날짜를 선택해주세요").setPositiveButton("확인",null).show();
                } else{
                    if(route_item.equals("세종")){ //세종은 1회차밖에 없기때문에 2회차선택레이아웃을 안보이게한다.
                        turn_select.setVisibility(View.VISIBLE);
                        first_turn.setVisibility(View.VISIBLE);
                        second_turn.setVisibility(View.GONE);
                    }else{
                        turn_select.setVisibility(View.VISIBLE);
                        first_turn.setVisibility(View.VISIBLE);
                        second_turn.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        first_reserve = findViewById(R.id.first_reserve);
        second_reserve = findViewById(R.id.second_reserve);
        first_reserve.setOnClickListener(new View.OnClickListener() { //1회차는 turn이라는 회차를 저장하는 변수에 1을 넘긴다
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
                String date = dateFormat.format(calendar.getTime()); //시간값을 시간타입에서 문자열 타입으로 바꾸기
                int turn = 1;//회차
                Intent next = new Intent(ReservationActivity.this, pickplaceActivity.class);
                next.putExtra("userID",userID); //유저아이디
                next.putExtra("turn",turn); //회차
                next.putExtra("select_route",route_item); //선택노선
                next.putExtra("select_date",date); //선택날짜
                startActivity(next);

            }
        });
        second_reserve.setOnClickListener(new View.OnClickListener() {//2회차는 turn이라는 회차를 저장하는 변수에 2을 넘긴다
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
                String date = dateFormat.format(calendar.getTime()); //시간값을 시간타입에서 문자열 타입으로 바꾸기
                int turn = 2;//회차
                Intent next = new Intent(ReservationActivity.this, pickplaceActivity.class);
                next.putExtra("userID",userID);
                next.putExtra("turn",turn);
                next.putExtra("select_route",route_item);
                next.putExtra("select_date",date);
                startActivity(next);
            }
        });


    }
    private void showDatePicker(){
        final Calendar currentDate = Calendar.getInstance();
        final Calendar dateLimit =  Calendar.getInstance();
        dateLimit.add(Calendar.DAY_OF_MONTH,3); //날짜선택가능수는 현재날짜에서부터 3일 현재날짜를 포함하면 4일이다
        //오늘날짜가 10일이면 10 11 12 13일을 선택가능하다.
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                updateSelectedDateButton();
            }
        },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(dateLimit.getTimeInMillis());
        datePickerDialog.show();
    }

    private void updateSelectedDateButton(){
        if(calendar != null){
            //알수없는 문자열로 된 날짜배열을 사람이 알기쉬운 년월일 순으로 보이게한다
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
            String selectedDateStr = dateFormat.format(calendar.getTime());
            //날짜선택한 버튼을 선택한 날짜의 텍스트로 바꾼다.
            select_date.setText(selectedDateStr);
        }
    }


}