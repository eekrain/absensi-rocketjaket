package com.haerul.androidregisterandlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LocationError extends AppCompatActivity {
    SessionManager sessionManager;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_error);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(LocationError.this, HomeActivity.class);
                startActivity(home);
            }
        });
    }
}
