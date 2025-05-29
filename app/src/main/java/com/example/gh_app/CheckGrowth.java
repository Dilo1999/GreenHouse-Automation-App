package com.example.gh_app;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CheckGrowth extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GrowthDataAdapter growthDataAdapter;
    private List<GrowthData> growthDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_growth);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns for grid view

        growthDataList = new ArrayList<>();
        growthDataAdapter = new GrowthDataAdapter(growthDataList);
        recyclerView.setAdapter(growthDataAdapter);

        // Fetch data from Firebase
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Growth");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                growthDataList.clear(); // Clear old data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve each Growth data node
                    String imageUrl = snapshot.child("image_url").getValue(String.class);
                    Log.d("URL", imageUrl);
                    String timestamp = snapshot.child("timestamp").getValue(String.class);
                    List<Double> beanLengths = new ArrayList<>();

                    for (DataSnapshot beanSnapshot : snapshot.child("bean_lengths").getChildren()) {
                        Double beanLength = beanSnapshot.child("bean_length_cm").getValue(Double.class);
                        if (beanLength != null) {
                            beanLengths.add(beanLength);
                        }
                    }

                    GrowthData growthData = new GrowthData(imageUrl, timestamp, beanLengths);
                    growthDataList.add(growthData);
                }
                growthDataAdapter.notifyDataSetChanged(); // Notify adapter to refresh RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CheckGrowth", "Error fetching data", databaseError.toException());
            }
        });
    }
}
