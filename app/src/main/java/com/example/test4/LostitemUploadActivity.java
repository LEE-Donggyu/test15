package com.example.test4;

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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LostitemUploadActivity extends AppCompatActivity {

    Bitmap bitmap;

    String userID;

    ImageView imageView;
    Button findimgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostitem_upload);

        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
        }

        imageView = findViewById(R.id.uploadimg);
        findimgButton = findViewById(R.id.findimgButton);

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

        findimgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        Button finaluploadButton = findViewById(R.id.finaluploadButton);

        finaluploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uploadID = userID;
                String uploadItem = ((EditText) findViewById(R.id.LostItemNameText)).getText().toString();
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
                        String url = "http://bestknow98.cafe24.com/upload.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("success")) {
                                            Toast.makeText(getApplicationContext(), "분실물 목록에 추가하였습니다.", Toast.LENGTH_SHORT).show();
                                            Intent successintent = new Intent(LostitemUploadActivity.this, LostItemActivity.class);
                                            startActivity(successintent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "분실물 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
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
                                params.put("userID", userID);
                                params.put("uploadItem", uploadItem);
                                params.put("uploadTime", uploadTime);
                                params.put("image", base64Image);
                                return params;
                            }
                        };
                        queue.add(stringRequest);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "이미지를 먼저 선택해주세요", Toast.LENGTH_SHORT).show();
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