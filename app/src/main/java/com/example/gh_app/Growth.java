package com.example.gh_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent; // Import for Intent
import android.os.Bundle;
import android.widget.Button; // Import for Button
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Growth extends AppCompatActivity {

    private Switch switch1, switch2, switch3;
    private TextView textView1, textView2, textView3;
    private Button nextButton; // Button to navigate to CheckGrowth
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth);

        // Initialize Firebase Realtime Database
        dbRef = FirebaseDatabase.getInstance().getReference();

        // Get references to the switches, text views, and button
        switch1 = findViewById(R.id.Humidity_switch);
        switch2 = findViewById(R.id.Temperature_switch);
        switch3 = findViewById(R.id.Soil_switch);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        nextButton = findViewById(R.id.next); // Initialize the button

        // Set listeners for switch state changes
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSwitchState("Humidity_switch", isChecked);
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSwitchState("Temperature_switch", isChecked);
            }
        });

        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSwitchState("Soil_switch", isChecked);
            }
        });

        // Set onClickListener for the button to navigate to CheckGrowth
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(Growth.this, CheckGrowth.class);
            startActivity(intent);
        });

        // Fetch sensor data from Realtime Database and update TextViews
        fetchSensorData();
    }

    private void saveSwitchState(String switchName, boolean isChecked) {
        // Create a Map to hold the switch state
        Map<String, Object> switchState = new HashMap<>();
        switchState.put(switchName, isChecked ? 1 : 0);

        // Save the switch state to Realtime Database
        dbRef.child("switchStates").child(switchName)
                .setValue(switchState)
                .addOnSuccessListener(aVoid -> {
                    // Successfully written to Realtime Database
                    Toast.makeText(Growth.this, switchName + " state saved", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to write to Realtime Database
                    Toast.makeText(Growth.this, "Error saving " + switchName + " state", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchSensorData() {
        dbRef.child("sensorData").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String humidity = dataSnapshot.child("HumiditySensor").getValue(String.class);
                    String temperature = dataSnapshot.child("TemperatureSensor").getValue(String.class);
                    String soil = dataSnapshot.child("SoilSensor").getValue(String.class);

                    textView1.setText("HumiditySensor: " + humidity);
                    textView2.setText("TemperatureSensor: " + temperature);
                    textView3.setText("SoilSensor: " + soil);
                } else {
                    Toast.makeText(Growth.this, "No sensor data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Growth.this, "Error fetching sensor data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
