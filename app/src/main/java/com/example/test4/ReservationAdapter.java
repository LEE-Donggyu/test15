package com.example.test4;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ReservationAdapter extends BaseAdapter{

    private Context context;
    private List<ReservationItem> ReservationList;
    public ReservationAdapter(Context context, List<ReservationItem> ReservationList){
        this.context = context;
        this.ReservationList = ReservationList;
    }

    @Override
    public int getCount(){
        return ReservationList.size();
    }

    @Override
    public Object getItem(int i) {
        return ReservationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.reserve, null);
        TextView startText = v.findViewById(R.id.bus_start);
        startText.setText(ReservationList.get(i).getStart());
        v.setTag(ReservationList.get(i).getStart());
        return v;
    }




}