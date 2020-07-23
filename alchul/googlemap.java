package com.example.hong.alchul;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hong.alchul.manager.MainActivity_manager;
import com.example.hong.alchul.parttime.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class googlemap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    String userId, userPassword, userName, userPhoneNum,userStat;
    String lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);
        Intent intent = getIntent();
        userId = intent.getStringExtra("UserId");
        userName = intent.getStringExtra("UserName");
        userPhoneNum = intent.getStringExtra("UserPhoneNum");
        userStat = intent.getStringExtra("UserStat");

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googlemap.this);


    }



    @Override
    public void onMapReady(final GoogleMap map) {
        mMap = map;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i("test", "click은됨");
                MarkerOptions mOption = new MarkerOptions();
                //title
                mOption.title("가게");
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;
                lat = latitude.toString();
                lon = longitude.toString();
                Log.i("test좌표",latitude.toString() );

                mOption.snippet(latitude.toString()+","+longitude.toString());

                //latlng: 위도 경도쌍
                mOption.position(new LatLng(latitude,longitude));
                map.addMarker(mOption);



            }
        });

        mMap.setOnMarkerClickListener(this);

        LatLng seoul = new LatLng(37.5197889, 126.9403083);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul,14));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(googlemap.this);
        builder.setMessage("이곳으로 일터를 지정하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(googlemap.this, MainActivity_manager.class);
                        intent.putExtra("UserId", userId);
                        intent.putExtra("UserName", userName);
                        intent.putExtra("UserPhoneNum", userPhoneNum);
                        intent.putExtra("UserStat", userStat);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lon", lon);
                        Log.i("test", lat);
                        startActivity(intent);
                    }
                })
                .create()
                .show();

        return false;
    }
}



