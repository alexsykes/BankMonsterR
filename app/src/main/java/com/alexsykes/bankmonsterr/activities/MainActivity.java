package com.alexsykes.bankmonsterr.activities;

// https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
// https://normanaspx.medium.com/android-room-how-works-one-to-many-relationship-example-e8a17531a3bb


import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.BMarker;
import com.alexsykes.bankmonsterr.data.MarkerDao;
import com.alexsykes.bankmonsterr.data.MarkerViewModel;
import com.alexsykes.bankmonsterr.data.WaterAndParents;
import com.alexsykes.bankmonsterr.data.WaterRoomDatabase;
import com.alexsykes.bankmonsterr.data.WaterViewModel;
import com.alexsykes.bankmonsterr.utility.WaterListAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMapLoadedCallback, OnMapReadyCallback {

    MarkerViewModel markerViewModel;
    List<BMarker> allBMarkers;
    private GoogleMap mMap;
    LinearLayout markerDetailLayout;
    TextView markerNameText, markerDetailText;
    Button saveButton;
    RecyclerView recyclerView;
    FloatingActionButton newMarkerButton;
    BMarker current;
    double curLng, curLat;
    int curr_id;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = "Info";
    private static final int DEFAULT_ZOOM = 15;
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-53.59470125308922, -2.5608564913272858);
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean locationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(this);
        MarkerDao markerDao = db.dao();

//        Set up UI components
        setContentView(R.layout.activity_main2);
        markerDetailLayout = findViewById(R.id.markerDetailLayout);
        markerDetailLayout.setVisibility(View.GONE);
        markerNameText = findViewById(R.id.markerNameText);
        markerDetailText = findViewById(R.id.markerDetailText);
        saveButton = findViewById(R.id.saveButton);
        saveButton.setVisibility(View.GONE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Info", "Save button clicked.");
                markerDao.updateMarker(curr_id, curLat, curLng);
                markerDetailLayout.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
//                int marker_id = current.getMarker_id();
//                double lat = current.getLat();
//                double lng = current.getLng();

                // markerDao.updateMarker(marker_id,lat,lng);
            }
        });
        newMarkerButton = findViewById(R.id.newMarkerButton);
        newMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: New Marker");
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        final WaterListAdapter adapter = new WaterListAdapter(new WaterListAdapter.WaterDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Load saved data
        WaterViewModel waterViewModel = new ViewModelProvider(this).get(WaterViewModel.class);
        LiveData<List<WaterAndParents>> waterandparents = waterViewModel.getWaterAndParentList();
        waterandparents.observe(this, adapter::submitList);

        getMarkers();
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

    /*  Set up map variables
        Add listeners
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.i("Info", "onMapReady: ");
        String marker_title, code, type;
        LatLng latLng;
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
                        int marker_id = Integer.valueOf(marker.getSnippet());
                        markerDetailText.setText("Lat: " + latStr + System.lineSeparator() + "Lng: " + lngStr
                                + System.lineSeparator() + "Marker id: " + snippet);

                        curLat = newpos.latitude;
                        curLng = newpos.longitude;
                        curr_id = Integer.valueOf(snippet);

                        //   current = new BMarker(marker_id, newpos.latitude, newpos.longitude);
                    }

                    @Override
                    public void onMarkerDragStart(@NonNull Marker marker) {
                        markerDetailLayout.setVisibility(View.VISIBLE);
                        saveButton.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);

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

//         Check if saved markers are present
//
        if (!allBMarkers.isEmpty()) {

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
        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        //updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    //  Utility methods
    void getMarkers() {
        markerViewModel = new ViewModelProvider(this).get(MarkerViewModel.class);
        allBMarkers = markerViewModel.getAllMarkers();
        Log.i("Info", "getMarkers: " + allBMarkers.size());
    }
}