package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private LocationManager lm;
    private MyListener listener;
    private TextView tv;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        tv = findViewById(R.id.loctv);
        listener = new MyListener(tv);
        lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 1, listener);


        lm.addGpsStatusListener(event -> {
            GpsStatus gps = lm.getGpsStatus(null);
            String ss = "status: \n";
            for (GpsSatellite sat : gps.getSatellites()) {
                String s = "PRN:" + sat.getPrn() + "\tSNR:" + sat.getSnr() + "\n";
                Log.i("satelliteStatus", s);
                ss = ss + s;
            }
            TextView statusBox = findViewById(R.id.StatusBox);
            statusBox.setText(ss);
        });

        lm.addNmeaListener((GpsStatus.NmeaListener) (timestamp, nmea) -> {
            TextView nmeaBox = findViewById(R.id.NmeaBox);
            if (nmea.contains("GPGSA")) {
                nmeaBox.setText(nmea);
            }
            Log.i("satelliteNMEA", nmea);
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void sendMessage(View view) {
        Location location = new Location("");
        EditText latitude = (EditText)findViewById(R.id.latitude);
        EditText longitude = (EditText)findViewById(R.id.longitude);
        String longitudeString = longitude.getText().toString();
        String latitudeString = latitude.getText().toString();
        if(longitudeString.length() != 0 && latitudeString.length() != 0){
            location.setLongitude(Double.parseDouble(longitudeString));
            location.setLatitude(Double.parseDouble(latitudeString));
            listener.addNewLocation(location);
        }

    }
}