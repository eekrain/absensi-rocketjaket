package com.haerul.androidregisterandlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

import com.haerul.androidregisterandlogin.LoginActivity;
import com.haerul.androidregisterandlogin.R;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {
    SessionManager sessionManager;
    String username;
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        HashMap<String, String> user = sessionManager.getUserDetail();
        username = user.get(sessionManager.EMAIL);

        boolean delayed = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intent);
            }
        }, SPLASH_TIME_OUT);
    }
}
