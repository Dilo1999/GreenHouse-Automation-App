package com.example.gh_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Deseas extends AppCompatActivity {

    private GridView gridView;
    private ArrayList<String> imageUrls; // List to store image URLs from Firebase
    private ImageAdapter imageAdapter; // Custom adapter to display images

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deseas);

        gridView = findViewById(R.id.gridView);
        imageUrls = new ArrayList<>();

        // Initialize the Firebase Database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Deseas");

        // Fetch data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageUrls.clear(); // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Fetch the "predicted_image_url" field instead of "imageUrl"
                    String imageUrl = snapshot.child("predicted_image_url").getValue(String.class);
                    if (imageUrl != null) {
                        imageUrls.add(imageUrl);
                    }
                }
                // Notify the adapter that data has changed
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
                Toast.makeText(Deseas.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the adapter
        imageAdapter = new ImageAdapter(Deseas.this, imageUrls);
        gridView.setAdapter(imageAdapter);
    }
}
