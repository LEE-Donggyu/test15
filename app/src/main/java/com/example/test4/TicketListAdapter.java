package com.example.test4;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TicketListAdapter extends BaseAdapter {

    private Context context;
    private List<Ticket> ticketList;

    public TicketListAdapter(Context context, List<Ticket> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
    }

    @Override
    public int getCount() {
        return ticketList.size();
    }

    @Override
    public Object getItem(int i) {
        return ticketList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View v = View.inflate(context, R.layout.ticket, null);

        TextView RouteName = v.findViewById(R.id.routename);
        TextView DateTicket = v.findViewById(R.id.DateTicket);
        TextView NameTicket = v.findViewById(R.id.NameTicket);
        TextView PickupTicket = v.findViewById(R.id.PickupnameTicket);
        TextView TurnTicket = v.findViewById(R.id.TurnTicket);

        RouteName.setText(ticketList.get(i).getRoutename());
        DateTicket.setText(ticketList.get(i).getDate());
        NameTicket.setText(ticketList.get(i).getUserName());
        PickupTicket.setText(ticketList.get(i).getPickupname());
        TurnTicket.setText(ticketList.get(i).getTurn());

        v.setTag(ticketList.get(i).getRoutename());

        return v;
    }
}
