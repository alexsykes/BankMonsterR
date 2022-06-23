package com.alexsykes.bankmonsterr.activities;

// https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
// https://normanaspx.medium.com/android-room-how-works-one-to-many-relationship-example-e8a17531a3bb


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.BMarker;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMapLoadedCallback, OnMapReadyCallback {

    MarkerViewModel markerViewModel;
    List<BMarker> allBMarkers;
    private GoogleMap mMap;
    LinearLayout markerDetailLayout;
    TextView markerNameText, markerDetailText;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        markerDetailLayout = findViewById(R.id.markerDetailLayout);
        markerDetailLayout.setVisibility(View.GONE);
        markerNameText = findViewById(R.id.markerNameText);
        markerDetailText = findViewById(R.id.markerDetailText);
        saveButton = findViewById(R.id.saveButtonbutton);
        saveButton.setVisibility(View.GONE);

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
        allBMarkers = markerViewModel.getAllMarkers();
        Log.i("Info", "getMarkers: " + allBMarkers.size());
    }

    public void onClickCalled(int id, String water_name) {
        Log.i("Info", "onClickCalled: " + water_name + id);
        List<BMarker> BMarkerList = markerViewModel.getMarkerList(id);
        if (!BMarkerList.isEmpty()) {
            LatLng latLng;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            int padding = 100;
            String code;

            for (BMarker BMarker : BMarkerList) {
                latLng = new LatLng(BMarker.getLat(), BMarker.getLng());
                builder.include(latLng);
                code = BMarker.getCode();
            }

            LatLngBounds bounds =
                    builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            if (BMarkerList.size() != 1) {
                // mMap.resetMinMaxZoomPreference();
            }
        } else {
            showAllMarkers();
        }
    }

    private void showAllMarkers() {
        if (!allBMarkers.isEmpty()) {
            LatLng latLng;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            int padding = 100;
            String code;

            for (BMarker BMarker : allBMarkers) {
                latLng = new LatLng(BMarker.getLat(), BMarker.getLng());
                builder.include(latLng);
                code = BMarker.getCode();
            }

            LatLngBounds bounds =
                    builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            if (allBMarkers.size() != 1) {
                // mMap.resetMinMaxZoomPreference();
            }
        }
    }


    @Override
    public void onMapLoaded() {
        Log.i("Info", "onMapLoaded: ");
        if (!allBMarkers.isEmpty()) {
            LatLng latLng;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            int padding = 200;
            String code;

            for (BMarker BMarker : allBMarkers) {
                latLng = new LatLng(BMarker.getLat(), BMarker.getLng());
                builder.include(latLng);
                code = BMarker.getCode();
            }

            LatLngBounds bounds =
                    builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
           // mMap.setMaxZoomPreference(18);
            if (allBMarkers.size() != 1) {
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
        mMap.setMinZoomPreference(8);
        mMap.setMaxZoomPreference(20);
        mMap.setOnMarkerDragListener(
                new GoogleMap.OnMarkerDragListener() {
                    final DecimalFormat df = new DecimalFormat("#.#####");
                    LatLng startPos, endPos;

                    @Override
                    public void onMarkerDrag(@NonNull Marker marker) {
                        LatLng newpos = marker.getPosition();
                        String snippet = marker.getSnippet();
                        String latStr = df.format(newpos.latitude);
                        String lngStr = df.format(newpos.longitude);
                        markerDetailText.setText("Lat: " + latStr + System.lineSeparator() + "Lng: " + lngStr
                                + System.lineSeparator() + "Marker id: " + snippet);
                    }

                    @Override
                    public void onMarkerDragEnd(@NonNull Marker marker) {
                        // markerDetailLayout.setVisibility(View.GONE);
                        LatLng newpos = marker.getPosition();
                        String snippet = marker.getSnippet();
                        String latStr = df.format(newpos.latitude);
                        String lngStr = df.format(newpos.longitude);
                        markerDetailText.setText("Lat: " + latStr + System.lineSeparator() + "Lng: " + lngStr
                                + System.lineSeparator() + "Marker id: " + snippet);
                    }

                    @Override
                    public void onMarkerDragStart(@NonNull Marker marker) {
                        markerDetailLayout.setVisibility(View.VISIBLE);
                        saveButton.setVisibility(View.VISIBLE);
                        markerNameText.setText(marker.getTitle());
                        startPos = marker.getPosition();
                        String snippet = marker.getSnippet();
                        String latStr = df.format(startPos.latitude);
                        String lngStr = df.format(startPos.longitude);
                        markerDetailText.setText("Lat: " + latStr + System.lineSeparator() + "Lng: " + lngStr
                                + System.lineSeparator() + "Marker id: " + snippet);

                    }
                }
        );
        mMap.setOnMarkerClickListener(
                marker -> {
                    // marker.setVisible(!marker.isVisible());
                    // marker.showInfoWindow();
                    return false;
                }
        );

        // mMap.setMaxZoomPreference(15);
        String marker_title;
        LatLng latLng;

        if (!allBMarkers.isEmpty()) {
            String code, type;

            for (BMarker BMarker : allBMarkers) {
                latLng = new LatLng(BMarker.getLat(), BMarker.getLng());
                code = BMarker.getCode();
                type = BMarker.getType();
                String snippet = String.valueOf(BMarker.getMarker_id());

                marker_title = BMarker.getName() + " " + code;
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(marker_title)
                        .snippet(snippet)
                        .visible(true);

                if (type.equals("Car Park")) {
                    markerOptions.visible(true);
                } else {
                    markerOptions.visible(true);
                }
                markerOptions.draggable(true);
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }
}