package com.haerul.androidregisterandlogin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProsesAbsenMasukActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    Button btnChoose, btnUpload;
    ImageView imageView;
    InputStream inputStream;
    Bitmap bitmap;
    Bitmap image;
    String urlUpload = "https://www.rocketjaket.com/api/Absennotshift/prosesAbsenMasuk";
    Uri image_uri;
    SessionManager sessionManager;
    String username;
    SimpleDateFormat sdf = new     SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
    String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_absen_masuk);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        imageView = findViewById(R.id.image_upload);
        btnChoose = findViewById(R.id.camerabtn);
        btnUpload = findViewById(R.id.btnupload);

        HashMap<String, String> user = sessionManager.getUserDetail();
        username = user.get(sessionManager.EMAIL);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent maps = new Intent(ProsesAbsenMasukActivity.this, AbsenMasukActivity.class);
                askCameraPermission();

            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error " +e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent(ProsesAbsenMasukActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"eror" + error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new  HashMap<>();
                        String imageData = imageToString(image);
//                        String imageData = "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";
                        params.put("image", imageData);
                        params.put("username", username);



                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(ProsesAbsenMasukActivity.this);
                requestQueue.add(stringRequest);
            }
        });

    }


    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);

        }else {
            openCamera();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else {
                Toast.makeText(this, "Perizinan kamera tidak disetujui. Anda harus allow akses kamera untuk absen.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            image = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image);

        }
    }
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }
}
