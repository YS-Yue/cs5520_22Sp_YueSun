package com.example.numad22sp_yuesun.at_your_service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.numad22sp_yuesun.R;
import com.google.android.material.textfield.TextInputLayout;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Locale;

public class AtYourServiceActivity extends AppCompatActivity {
    HolidayRecyclerViewAdapter viewAdapter;
    final ArrayList<HolidayItem> holidayItemsList = new ArrayList<>();
    static final String KEY_OF_HOLIDAYS = "KEY_OF_HOLIDAYS";
    static final String NUMBER_OF_HOLIDAYS = "NUMBER_OF_HOLIDAYS";
    ProgressBar progressBar;
    String countryCode = "CN";
    String year = "2022";
    final Handler uiHandler = new Handler();
    TextInputLayout textInputLayout;
    AutoCompleteTextView countryPicker;
    ArrayAdapter<String> arrayAdapterCountry;
    ArrayList<String> countryNameList = new ArrayList<>();
    HashMap<String, String> countryNameToCode = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);
        init(savedInstanceState);

        NumberPicker yearPicker = findViewById(R.id.year_number_picker);
        yearPicker.setMaxValue(2040);
        yearPicker.setMinValue(2000);
        yearPicker.setValue(2022);
        yearPicker.setWrapSelectorWheel(false);
        yearPicker.setOnValueChangedListener((numberPicker, i, i1) -> year = String.valueOf(i1));

        Button buttonGetHoliday = findViewById(R.id.button_check_holidays);
        buttonGetHoliday.setOnClickListener(this::onClickButtonGetHoliday);

        progressBar = findViewById(R.id.progressBar);

        populateCountryData();
        textInputLayout = findViewById(R.id.text_input_layout_country);
        countryPicker = findViewById(R.id.country_selected);
        arrayAdapterCountry = new ArrayAdapter<>(getApplicationContext(), R.layout.countries_textview,countryNameList);
        countryPicker.setAdapter(arrayAdapterCountry);
        countryPicker.setThreshold(1);
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

    private void populateCountryData() {
        String[] countriesList = Locale.getISOCountries();
        ArrayList<String> countryCodeArrayList = new ArrayList<>(Arrays.asList(countriesList));
        for (int i = 0; i < countryCodeArrayList.size(); i ++) {
            String currentCountryCode = countryCodeArrayList.get(i);
            String currentCountryName = new Locale("",currentCountryCode).getDisplayCountry();
            countryNameList.add(currentCountryName);
            countryNameToCode.put(currentCountryName, currentCountryCode);
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

    private void onClickButtonGetHoliday(View view) {
        holidayItemsList.clear();
        viewAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        AutoCompleteTextView countrySelected = findViewById(R.id.country_selected);
        String countryName = countrySelected.getText().toString();
        countryCode = countryNameToCode.get(countryName);
        runCallTread(view);
    }

    public void runCallTread(View view) {
         RunnableTread callThread = new RunnableTread();
         new Thread(callThread).start();
    }

    class RunnableTread implements Runnable {
        @Override
        public void run() {
            ArrayList<HolidayItem> queryResults = queryFromAPI();
            if (queryResults != null) {
                holidayItemsList.addAll(0,queryResults);
                uiHandler.post(() -> {
                    viewAdapter.notifyItemRangeInserted(0, queryResults.size());
                    progressBar.setVisibility(View.INVISIBLE);
                });
            }
        }
    }

    private ArrayList<HolidayItem> queryFromAPI() {
        String urlString = "https://date.nager.at/api/v3/PublicHolidays/" + year + "/" + countryCode;
        try {
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
                notifyUserTheQueryError("CountryCode is unknown. ");
                Log.e("NOT FOUND: ", conn.getErrorStream().toString());
                return null;
            } else if (conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
                notifyUserTheQueryError("No Content available for this country. ");
                Log.e("NO Content: ", "No content available for this country");
                return null;
            } else {
                notifyUserTheQueryError("Validation failure. ");
                Log.e("NOT Valid: ", conn.getErrorStream().toString());
                return null;
            }
        } catch (IOException e) {
            Log.e("Error of queryFromAPI", e.getMessage());
            notifyUserTheQueryError("Unable to fetch data from API. ");
            return null;
        } catch (JSONException e) {
            Log.e("Error of read JSON response", e.getMessage());
            notifyUserTheQueryError("Failed to read JSON response. ");
            return null;
        }
    }

    private void notifyUserTheQueryError(String s) {
        uiHandler.post(() -> {
            Toast.makeText(AtYourServiceActivity.this, s, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        });
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
