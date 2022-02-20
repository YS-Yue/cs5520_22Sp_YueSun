package com.example.numad22sp_yuesun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class LocatorActivity extends AppCompatActivity implements LocationListener{
    LocationManager locationManager;
    TextView latitudeTextView;
    TextView longitudeTextView;
    private static final int LOCATOR_REQUEST_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);

//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATOR_REQUEST_CODE);
            return;
        }

        updatedLocation();
    }

    @SuppressLint("MissingPermission")
    void updatedLocation() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (location != null) {
            renderUpdates(location);
        }
    }

    private void renderUpdates(Location location) {
        String latitude = "Latitude: " + location.getLatitude();
        String longitude = "Longitude: " + location.getLongitude();
        longitudeTextView.setText(longitude);
        latitudeTextView.setText(latitude);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String @NotNull [] permissions,
                                           int @NotNull [] grantResults) {
        if (requestCode == LOCATOR_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updatedLocation();
            } else {
                Toast.makeText(LocatorActivity.this, "Location permission denied. Update in settings.", Toast.LENGTH_SHORT).show();
                longitudeTextView.setText(R.string.longitude_permission_denied);
                latitudeTextView.setText(R.string.latitude_permission_denied);
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        renderUpdates(location);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}