package com.example.test4;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FindItemAdapter extends BaseAdapter {

    private Context context;
    private List<FindItem> findList;

    public FindItemAdapter(Context context, List<FindItem> FindList) {
        this.context = context;
        this.findList = FindList;
    }

    @Override
    public int getCount() {
        return findList.size();
    }

    @Override
    public Object getItem(int i) {
        return findList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.finditem, null);
        TextView itemText = v.findViewById(R.id.finditemText);
        TextView nameText = v.findViewById(R.id.nameText);
        TextView dateText = v.findViewById(R.id.dateText);
        ImageView imageView = v.findViewById(R.id.finditemImage);

        itemText.setText(findList.get(i).getItem());
        nameText.setText(findList.get(i).getName());
        dateText.setText(findList.get(i).getDate());
        String imagePath = "http://bestknow98.cafe24.com/" + findList.get(i).getImagepath();
        Picasso.get().load(imagePath).into(imageView);

        return v;
    }

}
