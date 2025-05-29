package com.example.gh_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Soil extends AppCompatActivity {

    private Spinner yearSpinner, automationFactorsSpinner;
    private EditText phEditText;
    private Button sendDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil);

        // Find spinners, EditText, TextView, and Button in the layout
        yearSpinner = findViewById(R.id.idYear);
        automationFactorsSpinner = findViewById(R.id.idAutomation_Factors);
        phEditText = findViewById(R.id.editTextPH);
        sendDataButton = findViewById(R.id.button2);

        // Populate the Year Spinner (2020 to 2030)
        List<String> yearList = new ArrayList<>();
        for (int i = 2020; i <= 2030; i++) {
            yearList.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Populate the Automation Factors Spinner
        List<String> automationList = new ArrayList<>();
        automationList.add("Irrigation system/Lighting Systems");  // Index 0
        automationList.add("Irrigation system/climate control");    // Index 1
        automationList.add("Irrigation system/climate control/Lighting Systems");  // Index 2
        ArrayAdapter<String> automationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, automationList);
        automationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        automationFactorsSpinner.setAdapter(automationAdapter);

        // Set a click listener on the button to send data when clicked
        sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to send data to the API
                sendDataToApi();
            }
        });
    }

    private void sendDataToApi() {
        // Get data from UI elements
        String selectedYear = yearSpinner.getSelectedItem().toString();
        int automationFactorIndex = automationFactorsSpinner.getSelectedItemPosition();
        String phValue = phEditText.getText().toString();
        TextView predictionTextView = findViewById(R.id.textView6); // Locate textView6 for displaying the result

        // Hardcoded sensor data
       // double environmentTemperature = 28.7;
       // double environmentHumidity = 72.3;
       // double sunlightIntensity = 1200.0;
        //double soilMoisture = 30.5;

        // Check if the PH value is entered
        if (phValue.isEmpty()) {
            Toast.makeText(this, "Please enter the PH value", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a JSON object to send to the API
        try {
            JSONObject postData = new JSONObject();
            postData.put("Year", selectedYear);
            postData.put("Automation Factors", automationFactorIndex);
            postData.put("Soil Ph (%)", phValue);
            //postData.put("Environment Temperature (Â°C)", environmentTemperature);
            //postData.put("Environment Humidity (%)", environmentHumidity);
            //postData.put("Sunlight Intensity (lux)", sunlightIntensity);
            //postData.put("Soil Moisture (%)", soilMoisture);

            // Send data to the API in a background thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Define the API URL
                        URL url = new URL("http://18.204.198.193:5000/mobile_data"); // Replace with your actual API URL
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                        httpURLConnection.setRequestProperty("Accept", "application/json");
                        httpURLConnection.setDoOutput(true);

                        // Write the JSON data to the output stream
                        try (OutputStream os = httpURLConnection.getOutputStream()) {
                            byte[] input = postData.toString().getBytes("utf-8");
                            os.write(input, 0, input.length);
                        }

                        // Read the API response
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }

                        // Parse the response JSON to get the predicted cost
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        String predictedCost = jsonResponse.optString("Predicted Building Cost");

                        // Update the UI with the predicted cost in the TextView
                        runOnUiThread(() -> {
                            predictionTextView.setText(predictedCost); // Display the result
                            Toast.makeText(Soil.this, "Prediction received!", Toast.LENGTH_SHORT).show();
                        });

                        httpURLConnection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(Soil.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create JSON data", Toast.LENGTH_SHORT).show();
        }
    }
}
