package com.example.test4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateItemAdapter extends BaseAdapter {

    private Context context;
    private List<UpdateItem> updateList;

    public UpdateItemAdapter(Context context, List<UpdateItem> updateList) {
        this.context = context;
        this.updateList = updateList;
    }

    @Override
    public int getCount() {
        return updateList.size();
    }

    @Override
    public Object getItem(int i) {
        return updateList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.finditemupdate, null);
        TextView itemText = v.findViewById(R.id.finditemText);
        TextView nameText = v.findViewById(R.id.nameText);
        TextView dateText = v.findViewById(R.id.dateText);
        ImageView imageView = v.findViewById(R.id.finditemImage);
        Button updateButton = v.findViewById(R.id.update);

        itemText.setText(updateList.get(i).getItem());
        nameText.setText(updateList.get(i).getName());
        dateText.setText(updateList.get(i).getDate());
        String imagePath = "http://bestknow98.cafe24.com/" + updateList.get(i).getImagepath();
        String id = updateList.get(i).getid();
        Picasso.get().load(imagePath).into(imageView);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = updateList.get(i).getItem();
                String name = updateList.get(i).getName();
                String date = updateList.get(i).getDate();
                String image = updateList.get(i).getImagepath();

                Intent intent = new Intent(context, uploaditemUpdate.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", id);
                intent.putExtra("item", item);
                intent.putExtra("name", name);
                intent.putExtra("date", date);
                intent.putExtra("image", image);
                context.startActivity(intent);
            }
        });

        return v;
    }


}
