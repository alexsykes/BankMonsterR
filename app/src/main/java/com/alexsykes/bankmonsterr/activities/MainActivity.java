package com.alexsykes.bankmonsterr.activities;

// https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
// https://normanaspx.medium.com/android-room-how-works-one-to-many-relationship-example-e8a17531a3bb


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.Marker;
import com.alexsykes.bankmonsterr.data.MarkerViewModel;
import com.alexsykes.bankmonsterr.data.WaterAndParents;
import com.alexsykes.bankmonsterr.data.WaterViewModel;
import com.alexsykes.bankmonsterr.utility.WaterListAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMapLoadedCallback, OnMapReadyCallback {

    MarkerViewModel markerViewModel;
    List<Marker> markerList;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final WaterListAdapter adapter = new WaterListAdapter(new WaterListAdapter.WaterDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        WaterViewModel waterViewModel = new ViewModelProvider(this).get(WaterViewModel.class);
        LiveData<List<WaterAndParents>> waterandparents = waterViewModel.getWaterAndParentList();
        waterandparents.observe(this, adapter::submitList);

        getMarkers();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    void getMarkers() {
        markerViewModel = new ViewModelProvider(this).get(MarkerViewModel.class);
        markerList = markerViewModel.getAllMarkers();
        Log.i("Info", "getMarkers: " + markerList.size());
//        RecyclerView rv = findViewById(R.id.markerRecyclerView);
//        final MarkerListAdapter adapter = new MarkerListAdapter(markerList);
//        rv.setAdapter(adapter);
//        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClickCalled(int id, String water_name) {
        // Check for connectivity
//            Intent intent = new Intent(MainActivity.this, WaterDetailActivity.class);
//            intent.putExtra("water_id", id);
//            intent.putExtra("water_name",water_name);
//            startActivity(intent);
    }


    @Override
    public void onMapLoaded() {
        // mMap.setMaxZoomPreference(15);
//
//        mMap.setOnMyLocationButtonClickListener(this);
//        mMap.setOnMyLocationClickListener(this);
//        enableMyLocation();
        Log.i("Info", "onMapLoaded: ");
        if (!markerList.isEmpty()) {
            LatLng latLng;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            int padding = 100;
            String code;

            for (Marker marker : markerList) {
                latLng = new LatLng(marker.getLat(), marker.getLng());
                builder.include(latLng);
                code = marker.getCode();
            }

            LatLngBounds bounds =
                    builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            if (markerList.size() != 1) {
                // mMap.resetMinMaxZoomPreference();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // mMap.setMyLocationEnabled(true);
        mMap.setOnMapLoadedCallback(this);

        // mMap.setMaxZoomPreference(15);
        String marker_title;
        LatLng latLng;

        if (!markerList.isEmpty()) {
            String code;

            for (Marker marker : markerList) {
                latLng = new LatLng(marker.getLat(), marker.getLng());
                code = marker.getCode();
                marker_title = marker.getName() + " " + code;
                mMap.addMarker(new MarkerOptions().position(latLng).title(marker_title));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }

    }
}