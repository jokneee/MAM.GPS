package com.example.lab3;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MyListener implements LocationListener {
    private Location prevLocation = null;
    private TextView tv;
    public ArrayList<Location> customLocations = new ArrayList<>();

    public MyListener(TextView tv) {
        this.tv = tv;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onLocationChanged(@NonNull Location location) {
        przetwarzajLokalizacje(location);
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {

    }

    @Override
    public void onFlushComplete(int requestCode) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("DefaultLocale")
    private void przetwarzajLokalizacje(Location location) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Current location: %f, %f\n", location.getLatitude(), location.getLongitude()));

        if (prevLocation != null) {
            sb.append(String.format("Previous location: %f, %f\n", prevLocation.getLatitude(), prevLocation.getLongitude()))
                    .append(String.format("Speed: %f\n", location.getSpeed()))
                    .append(String.format("Direction: %f\n", location.bearingTo(prevLocation)));
        }

        sb.append("Nearest point:")
                .append(findNearest(location));

        tv.setText(sb.toString());

        prevLocation = location;
    }


    private String findNearest(Location location) {
        if (customLocations.isEmpty()) {
            return "No points added";
        }

        ArrayList<LocationWithDistance> list = new ArrayList<>();
        for (Location customLocation:
             customLocations) {
            list.add(new LocationWithDistance(locationToString(customLocation), customLocation.distanceTo(location)));
        }

        Collections.sort(list, (u1, u2) -> u1.getDistance().compareTo(u2.getDistance()));

        return list.get(0).getLocationString();
    }

    public void addNewLocation(Location location) {
        customLocations.add(location);
    }

    public String locationToString(Location location) {
        return String.format("(%s, %s)", location.getLatitude(), location.getLongitude());
    }

    public class LocationWithDistance{
        public String getLocationString() {
            return locationString;
        }

        private String locationString;

        public Float getDistance() {
            return distance;
        }

        private Float distance;


        public LocationWithDistance(String locationString, float distance) {
            this.locationString = locationString;
            this.distance = distance;
        }
    }
}
