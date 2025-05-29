package com.example.gh_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.gh_app.MainActivity;

public class splashscreen extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 1000; // 1 second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // Hide the ActionBar (app name) while the splash screen is displayed
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                try {
                    Intent mainIntent = new Intent(splashscreen.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
