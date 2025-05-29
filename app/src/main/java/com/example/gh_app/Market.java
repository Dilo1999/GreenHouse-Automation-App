package com.example.gh_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalTime;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import com.google.firebase.database.*;
import androidx.annotation.NonNull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.AsyncTask;
public class Market extends AppCompatActivity {

    private Spinner yearSpinner;
    private Spinner countrySpinner;
    private LineChart lineChart;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int[] datapoint = new int[3];

    String countrynumberonspinner = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        yearSpinner = findViewById(R.id.idYearSpinner);
        countrySpinner = findViewById(R.id.idCountrySpinner);
        lineChart = findViewById(R.id.chart);

        // Populate the year spinner with year options
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"2017", "2018" ,  "2019", "2020", "2021", "2022", "2023", "2024", "2025","2026","2027","2028","2029"});
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);


        // Populate the country spinner with country options
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Italy", "United Kingdom", "Australia", "France","Canada","Austria","Sweden","Taiwan","Japan"});
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);


        String[] stringArray = new String[9];
        float[] floatArray = new float[9];




        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected country number (position starts from 0)
                String countryNumber = "" + (position + 1);

                // Show a Toast message with the selected country number
                //Toast.makeText(Market.this, "Selected: " + countryNumber, Toast.LENGTH_SHORT).show();
                countrynumberonspinner=countryNumber;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Optional: Handle the case where no item is selected
            }
        });




        // Set listener for spinner
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYearStr = yearSpinner.getSelectedItem().toString();
                int selectedYear = Integer.parseInt(selectedYearStr);

                LocalTime currentTime = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                final String formattedTime = currentTime.format(formatter);

                Log.d("TAG", "Current Time: " + formattedTime);

                AsyncTask<Void, Void, Void> apiTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            for (int i = 3; i > 0; i--) {
                                sendYearToAPI(selectedYear - i, formattedTime);
                            }
                            sendYearToAPI(selectedYear, formattedTime);
                            for (int i = 1; i < 4; i++) {
                                sendYearToAPI(selectedYear + i, formattedTime);
                                Thread.sleep(1000); // 1 second delay between API calls
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        // Now perform Firebase data retrieval
                        String databaseUrl = "https://newa-82463-default-rtdb.firebaseio.com";
                        String collectionName = "price";
                        String documentId = formattedTime;

                        FirebaseDatabase database = FirebaseDatabase.getInstance(databaseUrl);
                        DatabaseReference ref = database.getReference().child(collectionName).child(documentId);

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @SuppressLint("StaticFieldLeak")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    ArrayList<String> fieldNames = new ArrayList<>();
                                    ArrayList<Object> fieldValues = new ArrayList<>();

                                   // Object data = dataSnapshot.getValue();

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String fieldName = snapshot.getKey(); // Field name
                                        Object value = snapshot.getValue();   // Field value

                                        // Add field name and value to respective arrays
                                        fieldNames.add(fieldName);
                                        fieldValues.add(value);
                                    }

                                    for (int i = 0; i < fieldNames.size(); i++) {
                                        Log.d("TAG", "Field: " + fieldNames.get(i) + ", Value: " + fieldValues.get(i));
                                        String valueString = fieldValues.get(i).toString(); // Assuming fieldValues.get(i) returns a Double
// Remove non-numeric characters except for the decimal point
                                        String numericString = valueString.replaceAll("[^\\d.]", "");
                                        Log.d("TAG", "Field: " + fieldNames.get(i) + ", Value: " + numericString);

                                        try {
                                            float floatValue = Float.parseFloat(numericString);
                                            // Now floatValue contains the float representation of the parsed string
                                            // Use floatValue as needed
                                            //updateChart(fieldNames.get(i),floatValue);
                                            stringArray[i]=fieldNames.get(i);
                                            floatArray[i]=floatValue;

                                        } catch (NumberFormatException e) {
                                            // Handle the case where numericString is not a valid float representation
                                            Log.e("TAG", "Error parsing float value: " + e.getMessage());
                                        }

                                        Log.d("TAG", "Field: " + fieldNames.get(i) + ", Value: " );

                                    }


                                    //Log.d("TAG", "Retrieved data for document " + documentId + ": " + data);
                                    // Update UI with retrieved data if needed
                                } else {
                                    Log.d("TAG", "No such document");
                                    // Handle case where document does not exist
                                }
                                updateChart(stringArray,floatArray);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.w("TAG", "Error retrieving data: " + databaseError.getMessage());
                                // Handle possible errors
                            }
                        });
                    }
                };

                // Execute AsyncTask
                apiTask.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }

        });


        // Load initial chart data
        //updateChart(2002); // Provide an initial year
        String selectedYear = "2024"; // Example year
        Float value = 1500.75f; // Example value

// Call the updateChart method
        //updateChart(selectedYear, value);
    }

    private void updateChart(String[] selectedYears, float[] values) {
        if (selectedYears == null || values == null || selectedYears.length != values.length) {
            throw new IllegalArgumentException("Invalid input arrays.");
        }

        List<Entry> entries = new ArrayList<>();

        // Populate entries list
        for (int i = 0; i < selectedYears.length; i++) {
            try {
                float year = Float.parseFloat(selectedYears[i].trim());
                float value = values[i];
                entries.add(new Entry(year, value));
            } catch (NumberFormatException | NullPointerException e) {
                // Handle parsing errors or null values here
                e.printStackTrace(); // Or log the error
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Market Data");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        Description description = new Description();
        description.setText("Market Analysis");
        lineChart.setDescription(description);

        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int intValue = (int) value;
                return String.valueOf(intValue);
            }
        });
        xAxis.setGranularity(1f);

        lineChart.invalidate();
    }









    private int getMarketValueForYear(int year) {
        return (int) (Math.random() * 10);
    }










    private void sendYearToAPI(int selectedYear2, String x) {
        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.43.218:5000/predict");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Variables for dynamic assignment
                int demand = 3; // Default value for all years
                int environmentalReason = 1; // Default value for all years
                double inflationRate = 4.5; // Default value for all years

                // Example conditions for dynamic value assignment
                if (selectedYear2 >= 2020 && selectedYear2 <= 2030) {
                    // Specific values based on selectedYear2
                    switch (selectedYear2) {
                        case 2021:
                            demand = 3;
                            environmentalReason = 1;
                            inflationRate = 4.6;
                            break;
                        case 2022:
                            demand = 2;
                            environmentalReason = 2;
                            inflationRate = 5.9;
                            break;
                        case 2023:
                            demand = 2;
                            environmentalReason = 3;
                            inflationRate = 3.5;
                            break;
                        case 2024:
                            demand = 2;
                            environmentalReason = 1;
                            inflationRate = 5.5;
                            break;
                        case 2025:
                            demand = 1;
                            environmentalReason = 1;
                            inflationRate = 4.0;
                            break;
                        case 2026:
                            demand = 1;
                            environmentalReason = 2;
                            inflationRate = 3.5;
                            break;
                        case 2027:
                            demand = 3;
                            environmentalReason = 1;
                            inflationRate = 3.5;
                            break;
                        case 2028:
                            demand = 2;
                            environmentalReason = 2;
                            inflationRate = 4.1;
                            break;
                        case 2029:
                            demand = 2;
                            environmentalReason = 2;
                            inflationRate = 2.0;
                            break;
                        case 2030:
                            demand = 2;
                            environmentalReason = 2;
                            inflationRate = 3.5;
                            break;
                        // Add more cases here if specific year-based changes are needed.
                        default:
                            // Default values if no specific case is matched
                            demand = 3;
                            environmentalReason = 1;
                            inflationRate = 4.5;
                            break;
                    }
                }

                // Create JSON object with the dynamically assigned values
                JSONObject jsonParam = new JSONObject();
                //Log.d("test", "sendYearToAPI:"+countrynumberonspinner);
                jsonParam.put("Year", selectedYear2);
                jsonParam.put("Country", countrynumberonspinner);
                jsonParam.put("Demand", demand);
                jsonParam.put("Environmental reasons", environmentalReason);
                jsonParam.put("Inflatation rate", inflationRate);
                jsonParam.put("documentid", x);

                // Log the JSON data being sent
                Log.d("API Request", "Sending data: " + jsonParam.toString());

                // Write JSON data to output stream
                OutputStream os = conn.getOutputStream();
                os.write(jsonParam.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                Log.d("API Response", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Handle success
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Data sent successfully", Toast.LENGTH_SHORT).show();
                        // You can perform any other UI update or action here
                    });
                } else {
                    // Handle unsuccessful API call
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Failed to send data. Response Code: " + responseCode, Toast.LENGTH_SHORT).show();
                        // You can perform any other UI update or action here
                    });
                }

                conn.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Exception occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Handle exception, if needed
                });
            }
        }).start();
    }






}
