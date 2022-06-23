package com.alexsykes.bankmonsterr.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.BMarker;
import com.alexsykes.bankmonsterr.data.MarkerViewModel;
import com.alexsykes.bankmonsterr.utility.MarkerListAdapter;
import com.alexsykes.bankmonsterr.utility.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class WaterDetailActivity extends AppCompatActivity implements
        GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{

    public static final String EXTRA_REPLY = "REPLY";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;

    private GoogleMap mMap;
    private EditText editWaterView;
    int water_id;
    int height, width;
    String water_name;
    MarkerViewModel markerViewModel;
    List<BMarker> BMarkerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_detail);
        markerViewModel = new ViewModelProvider(this).get(MarkerViewModel.class);
        RecyclerView rv = findViewById(R.id.markerRecyclerView);
        Intent intent = getIntent();
        water_id = intent.getIntExtra("water_id", -999);
        water_name = intent.getStringExtra("water_name");
        setTitle(water_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = (int) (0.6 * displayMetrics.heightPixels);
        width = (int) (0.6 * displayMetrics.widthPixels);


        getMarkers();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    void getMarkers() {
        BMarkerList = markerViewModel.getMarkerList(water_id);
        RecyclerView rv = findViewById(R.id.markerRecyclerView);
        final MarkerListAdapter adapter = new MarkerListAdapter(BMarkerList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onMapLoaded() {
         // mMap.setMaxZoomPreference(15);

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        Log.i("Info", "onMapLoaded: ");
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
        }
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            return;
        }

        // 2. Otherwise, request location permissions from the user.
      //  PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // mMap.setMyLocationEnabled(true);
        mMap.setOnMapLoadedCallback(this);

        // mMap.setMaxZoomPreference(15);
        String marker_title;
        LatLng latLng;

        if (!BMarkerList.isEmpty()) {
            String code;

            for (BMarker BMarker : BMarkerList) {
                latLng = new LatLng(BMarker.getLat(), BMarker.getLng());
                code = BMarker.getCode();
                marker_title = BMarker.getName() + " " + code;
                mMap.addMarker(new MarkerOptions().position(latLng).title(marker_title));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}