package com.example.lifecycle.service;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.lifecycle.AppApplication;
import com.example.lifecycle.util.TimeUtil;
import com.higyon.myapplication.appcomm.greendao.LocationDao;

public class GeoService1 extends Service implements LocationListener {
    private LocationManager locationManager;
    private LocationDao locationDao= AppApplication.getDaoSession().getLocationDao();;
    private LocationListener locationListener= this;
    public static final String TAG = "LocationListenerService";
    public GeoService1() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                // Check if the location service is on
                if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null
                        || locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
                    Log.i(TAG, "Locating..");
                    // Permission check
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return super.onStartCommand(intent, flags, startId);
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10,locationListener );
                }

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "LocationChanged");
        double latitude = location.getLatitude();
        Log.i(TAG, "latitude =" + latitude);
        double longitude = location.getLongitude();
        Log.i(TAG, "longitude =" + longitude);
        String date = TimeUtil.getCurrentDate();
        com.example.lifecycle.bean.Location location1 = new com.example.lifecycle.bean.Location(null,date,latitude,longitude);
        locationDao.insert(location1);
        Toast.makeText( getApplicationContext(),"latitude =" + latitude+"longitude =" + longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"destory");
    }
}
