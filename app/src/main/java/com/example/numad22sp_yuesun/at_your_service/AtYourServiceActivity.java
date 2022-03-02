package com.example.numad22sp_yuesun.at_your_service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.numad22sp_yuesun.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class AtYourServiceActivity extends AppCompatActivity {
    private HolidayRecyclerViewAdapter viewAdapter;
    private final ArrayList<HolidayItem> holidayItemsList = new ArrayList<>();
    private static final String KEY_OF_HOLIDAYS = "KEY_OF_HOLIDAYS";
    private static final String NUMBER_OF_HOLIDAYS = "NUMBER_OF_HOLIDAYS";
    private ProgressBar progressBar;
    private final String countryCode = "CN";
    private final String year = "2022";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);
        init(savedInstanceState);
        Button buttonGetHoliday = findViewById(R.id.button_check_holidays);
        buttonGetHoliday.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            runCallTread(view);
        });
        progressBar = findViewById(R.id.progressBar);
    }

    private void init(Bundle savedInstanceState) {
        initialHolidayItemsData(savedInstanceState);
        createRecyclerView();
    }

    private void initialHolidayItemsData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_HOLIDAYS)) {
            if (holidayItemsList.size() == 0 ) {
                int size = savedInstanceState.getInt(NUMBER_OF_HOLIDAYS);

                for (int i = 0; i < size; i++) {
                    String date = savedInstanceState.getString(KEY_OF_HOLIDAYS + i + "0");
                    String localName = savedInstanceState.getString(KEY_OF_HOLIDAYS + i + "1");
                    String name = savedInstanceState.getString(KEY_OF_HOLIDAYS + i + "2");
                    Boolean isFixed = savedInstanceState.getBoolean(KEY_OF_HOLIDAYS + i + "3");
                    String countryCode = savedInstanceState.getString(KEY_OF_HOLIDAYS + i + "4");

                    if (date != null && localName != null && name != null && countryCode != null) {
                        HolidayItem holidayItem = new HolidayItem(date, localName, name, isFixed, countryCode);
                        holidayItemsList.add(holidayItem);
                    }
                }
            }
        }
    }

    private void createRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView holidayRecyclerView = findViewById(R.id.holidays_recycler_view);
        holidayRecyclerView.setHasFixedSize(true);
        viewAdapter = new HolidayRecyclerViewAdapter(holidayItemsList);
        holidayRecyclerView.setAdapter(viewAdapter);
        holidayRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int size = holidayItemsList.size();
        outState.putInt(NUMBER_OF_HOLIDAYS, size);
        for (int i = 0; i < size; i++) {
            outState.putString(KEY_OF_HOLIDAYS + i + "0", holidayItemsList.get(i).getDate());
            outState.putString(KEY_OF_HOLIDAYS + i + "1", holidayItemsList.get(i).getLocalName());
            outState.putString(KEY_OF_HOLIDAYS + i + "2", holidayItemsList.get(i).getName());
            outState.putBoolean(KEY_OF_HOLIDAYS + i + "3", holidayItemsList.get(i).getFixed());
            outState.putString(KEY_OF_HOLIDAYS + i + "4", holidayItemsList.get(i).getCountryCode());
        }
        super.onSaveInstanceState(outState);
    }

    public void runCallTread(View view) {
         RunnableTread callThread = new RunnableTread();
         new Thread(callThread).start();
    }

    class RunnableTread implements Runnable {
        @Override
        public void run() {
            try {
                ArrayList<HolidayItem> queryResults = queryFromAPI();
                for (int i = 0; i < queryResults.size(); i++) {
                    holidayItemsList.add(queryResults.get(i));
                    int position = holidayItemsList.size() - 1;
                    viewAdapter.notifyItemInserted(position+1);
                }
            } catch (IOException e) {
                Log.e("IOE Error", "IOE Error");
                Toast.makeText(getApplicationContext(), "Failed to fetch data from Nager.Date API. ", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Log.e("JSONExceptionError", "JSONExceptionError");
            } finally {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    private ArrayList<HolidayItem> queryFromAPI() throws IOException, JSONException {
        String urlString = "https://date.nager.at/api/v3/PublicHolidays/" + year + "/" + countryCode;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = conn.getInputStream();
            final String response = convertStreamToString(inputStream);
            return convertToHolidayItems(response);
        } else if (conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            Toast.makeText(getApplicationContext(), "CountryCode is unknown. ", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        } else {
            Toast.makeText(getApplicationContext(), "Validation failure. ", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }
    }

    @NotNull
    private ArrayList<HolidayItem> convertToHolidayItems(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        ArrayList<HolidayItem> queryResults = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i ++) {
            JSONObject holidayJsonObj = jsonArray.getJSONObject(i);
            String date = holidayJsonObj.getString("date");
            String localName = holidayJsonObj.getString("localName");
            String name = holidayJsonObj.getString("name");
            Boolean isFix = holidayJsonObj.getBoolean("fixed");
            String countryCode = holidayJsonObj.getString("countryCode");
            HolidayItem singleHoliday = new HolidayItem(date, localName, name, isFix,countryCode);
            queryResults.add(singleHoliday);
        }
        return queryResults;
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}