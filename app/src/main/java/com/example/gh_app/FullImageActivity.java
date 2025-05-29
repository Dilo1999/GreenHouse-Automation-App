package com.example.gh_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        // Get the image URL from the intent
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Initialize ImageView
        ImageView fullImageView = findViewById(R.id.fullImageView);

        // Load the image using Glide or Picasso
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(fullImageView);
        }
    }
}
