package com.thecodecity.locationTracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;

    private static final int GPS_TIME_INTERVAL = 1000 * 60 * 5;                             // get gps location every 1 min
    private static final int GPS_DISTANCE = 1000;                                           // set the distance value in meter
    private static final int HANDLER_DELAY = 1000 * 60 * 5;
    private static final int START_HANDLER_DELAY = 0;

    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    final static int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) { requestPermissions(PERMISSIONS, PERMISSION_ALL); }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        delayerHandler();

    }

    /* M Osama: update location coordinates on location Changed */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("OsOs", "Got Location: " + location.getLatitude() + ", " + location.getLongitude());                                                      /* M Osama: to assure for us that it's working */
        Toast.makeText(MainActivity.this, "Got Coordinates: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();  /* M Osama: to be used to track user */
        locationManager.removeUpdates(this);
    }

    /* M Osama: start tracking if user accepted that his mobile's location can be used */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)  delayerHandler();
        else  finish();
    }

    /* M Osama: to ask the user to enable using his mobile Location */
    private void requestLocation() {
        if (locationManager == null)  locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_TIME_INTERVAL, GPS_DISTANCE, this);
            }

        }
    }

    /* M Osama: to send current location every 5 minutes */
    public void delayerHandler(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() { requestLocation(); handler.postDelayed(this, HANDLER_DELAY); }
        }, START_HANDLER_DELAY);
    }

}


