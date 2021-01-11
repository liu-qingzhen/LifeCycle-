package com.example.lifecycle.Footprinting;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lifecycle.AppApplication;
import com.example.lifecycle.R;
import com.example.lifecycle.bean.Location;

import com.example.lifecycle.service.GeoService1;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;



import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.higyon.myapplication.appcomm.greendao.LocationDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {
    private LocationDao locationDao;
    private List<Location> list;
    private ArrayList<Double> longtitude = new ArrayList<>();
    private ArrayList<Double> latitude = new ArrayList<>();
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;
    private GoogleMap map;
    private static final int POLYLINE_STROKE_WIDTH_PX = 20;

    private String date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.footprinting);
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){//permission not allowed
            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }else{
            startServer();//start getting location
            Toast.makeText(MapsActivity.this,"Permission Allowed",Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        // move camera to UCSB
        LatLng latlng = new LatLng( 34.413963,-119.848946);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng,17);
        googleMap.animateCamera(cameraUpdate);
        locationDao = AppApplication.getDaoSession().getLocationDao();
        Intent intent = getIntent();
        date = intent.getStringExtra("extra_data");
        list = locationDao.queryBuilder().where(LocationDao.Properties.Date.eq(date)).list();

        //Draw footprint
        for (int i =0;i<list.size();i++){
            longtitude.add(list.get(i).getY());
            latitude.add(list.get(i).getX());
            LatLng laglng = new LatLng(list.get(i).getX(),list.get(i).getY());
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(laglng,17);
            googleMap.animateCamera(cameraUpdate);
            Log.d("abc",latitude+"");
        }
        if(latitude.size()>=2){
            for(int i=1;i<latitude.size()-1;i++){
                Polyline footprint=map.addPolyline(new PolylineOptions().
                        add(new LatLng(latitude.get(i),longtitude.get(i)),
                                new LatLng(latitude.get(i+1),longtitude.get(i+1))
                        ));
                DrawFootprint(footprint);}
        }


    }    private void DrawFootprint(Polyline polyline) {
                polyline.setStartCap(new RoundCap());
                polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
                polyline.setColor(getResources().getColor(R.color.light_blue));
    }
    public void startServer(){
        Intent intent = new Intent(MapsActivity.this, GeoService1.class);
        startService(intent);
    }
    @Override
    public void onPolygonClick(Polygon polygon) {
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }
}

