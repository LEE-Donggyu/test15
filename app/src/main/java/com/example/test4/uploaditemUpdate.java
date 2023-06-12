package com.example.test4;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class uploaditemUpdate extends AppCompatActivity {

    private String id;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaditem_update);

        Button updateButton = findViewById(R.id.itemupdateButton);
        Button updateimageButton = findViewById(R.id.updateimgButton);
        EditText editText = findViewById(R.id.LostItemNameText);
        ImageView imageView = findViewById(R.id.updateimg);

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            String item = intent.getStringExtra("item");
            String name = intent.getStringExtra("name");
            String date = intent.getStringExtra("date");
            String image = intent.getStringExtra("image");

            editText.setText(item);
            Picasso.get().load("http://bestknow98.cafe24.com/" + image).into(imageView);
        }

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent odata = result.getData();
                    Uri uri = odata.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        updateimageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uploadItem = editText.getText().toString();
                String uploadTime = getCurrentTime();
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();

                if(uploadItem.equals("")){
                    Toast.makeText(getApplicationContext(), "분실물의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (bitmap != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        String url = "http://bestknow98.cafe24.com/update.php";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("success")) {
                                            Toast.makeText(getApplicationContext(), "분실물이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "분실물 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("id", id);
                                params.put("uploadItem", uploadItem);
                                params.put("uploadTime", uploadTime);
                                params.put("image", base64Image);

                                return params;
                            }
                        };
                        queue.add(stringRequest);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "이미지를 다시 선택해주세요", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }
}
