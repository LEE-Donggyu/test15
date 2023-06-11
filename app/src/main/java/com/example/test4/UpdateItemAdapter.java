package com.example.test4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
        Button deleteButton = v.findViewById(R.id.delete);

        itemText.setText(updateList.get(i).getItem());
        nameText.setText(updateList.get(i).getName());
        dateText.setText(updateList.get(i).getDate());
        String imagePath = "http://bestknow98.cafe24.com/" + updateList.get(i).getImagepath();
        String id = updateList.get(i).getid();
        Picasso.get().load(imagePath).into(imageView);


        // 삭제 버튼
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context); // context를 사용
                builder.setTitle("알림");
                builder.setMessage("정말로 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 삭제 이벤트 실행
                        deleteItemFromDatabase(id);
                    }
                });
                builder.setNegativeButton("아니요", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String item = updateList.get(i).getItem();
                String name = updateList.get(i).getName();
                String date = updateList.get(i).getDate();
                String image = updateList.get(i).getImagepath();

                // 아이템 수정 버튼 클릭 시 LostitemUploadActivity로 이동
                Intent intent = new Intent(context, uploaditemUpdate.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // FLAG_ACTIVITY_NEW_TASK 플래그 추가
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
    private void deleteItemFromDatabase(String id) {
        String url = "http://bestknow98.cafe24.com/deleteitem.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            // 삭제 후 필요한 동작 수행
                        } else {
                            Toast.makeText(context, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.add(stringRequest);
    }
}
