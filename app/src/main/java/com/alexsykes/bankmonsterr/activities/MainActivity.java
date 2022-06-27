package com.alexsykes.bankmonsterr.activities;

// https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
// https://normanaspx.medium.com/android-room-how-works-one-to-many-relationship-example-e8a17531a3bb


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.BMarker;
import com.alexsykes.bankmonsterr.data.MarkerDao;
import com.alexsykes.bankmonsterr.data.MarkerViewModel;
import com.alexsykes.bankmonsterr.data.Parent;
import com.alexsykes.bankmonsterr.data.ParentDao;
import com.alexsykes.bankmonsterr.data.Water;
import com.alexsykes.bankmonsterr.data.WaterAndParents;
import com.alexsykes.bankmonsterr.data.WaterDao;
import com.alexsykes.bankmonsterr.data.WaterRoomDatabase;
import com.alexsykes.bankmonsterr.data.WaterViewModel;
import com.alexsykes.bankmonsterr.utility.WaterListAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMapLoadedCallback, OnMapReadyCallback {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = "Info";
    private static final int DEFAULT_ZOOM = 15;
    boolean canConnect;
    MarkerViewModel markerViewModel;
    List<BMarker> bMarkerList;
    LinearLayout markerDetailLayout;
    TextView markerNameText, markerDetailText;
    Button saveButton;
    RecyclerView recyclerView;
    FloatingActionButton newMarkerButton;
    BMarker current;
    double curLng, curLat;
    int curr_id;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private LatLng defaultLocation;
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
        getAllMarkers();


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
                markerDao.updateMarker(curr_id, curLat, curLng, true);
                markerDetailLayout.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                syncChangedData();
            }
        });
        newMarkerButton = findViewById(R.id.newMarkerButton);
        newMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO - Dialog goes here
                Log.i(TAG, "onClick: New Marker");
                LatLng centre = mMap.getCameraPosition().target;

                MarkerOptions newMarker = new MarkerOptions()
                        .position(centre)
                        .draggable(true);
                mMap.addMarker(newMarker);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        final WaterListAdapter adapter = new WaterListAdapter(new WaterListAdapter.WaterDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        canConnect = canConnect();
        if (canConnect) {
            Log.i("Info", "Can connect");
            getSavedData();
        } else {
            Log.i("Info", "Cannot connect");
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // mapFragment.getMapAsync(this);

        // Load saved data
        WaterViewModel waterViewModel = new ViewModelProvider(this).get(WaterViewModel.class);
        LiveData<List<WaterAndParents>> waterandparents = waterViewModel.getWaterAndParentList();
        waterandparents.observe(this, adapter::submitList);
    }

    // Called from WaterViewHolder
    public void onWaterListItemClicked(int id, String water_name) {
        Log.i("Info", "onClickCalled: " + water_name + id);

        // Get markerList
        List<BMarker> markerListForWater = markerViewModel.getMarkerListForWater(id);

        // Display markerList on map
        addMarkerListToMap(markerListForWater);

        // Update Camera position
//        if (!markerListForWater.isEmpty()) {
//            mMap.clear();
//            addMarkerListToMap(markerListForWater);
//            LatLng latLng;
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            int padding = 100;
//            String code;
//
//            for (BMarker BMarker : markerListForWater) {
//                latLng = new LatLng(BMarker.getLat(), BMarker.getLng());
//                builder.include(latLng);
//                code = BMarker.getCode();
//                MarkerOptions markerOptions = new MarkerOptions()
//                        .position(latLng);
//                mMap.addMarker(markerOptions);
//            }
//
//            LatLngBounds bounds =
//                    builder.build();
//            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
//            if (markerListForWater.size() != 1) {
//                // mMap.resetMinMaxZoomPreference();
//            }
//        } else {
//            showAllMarkers();
//        }
    }

    /*  Set up map variables
        Add listeners
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.i("Info", "onMapReady: ");
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
        mMap.setMinZoomPreference(8);
        mMap.setMaxZoomPreference(20);

        //MARK: MarkerDragListener
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

                        curLat = newpos.latitude;
                        curLng = newpos.longitude;

                        if (snippet != null) {
                            curr_id = Integer.valueOf(snippet);
                        } else {

                        }

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

        // MarkerClickListener
        mMap.setOnMarkerClickListener(
                marker -> {
                    // marker.setVisible(!marker.isVisible());
                    // marker.showInfoWindow();
                    return false;
                }
        );

        addMarkerListToMap(bMarkerList);

        getLocationPermission();
        getDeviceLocation();
    }

    @Override
    public void onMapLoaded() {
        Log.i("Info", "onMapLoaded: ");
        if (!bMarkerList.isEmpty()) {
            LatLng latLng;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            int padding = 200;
            String code;

            for (BMarker BMarker : bMarkerList) {
                latLng = new LatLng(BMarker.getLat(), BMarker.getLng());
                builder.include(latLng);
                code = BMarker.getCode();
            }
            LatLngBounds bounds =
                    builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            // mMap.setMaxZoomPreference(18);
            if (bMarkerList.size() != 1) {
            }
        }
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
    void getAllMarkers() {
        markerViewModel = new ViewModelProvider(this).get(MarkerViewModel.class);
        bMarkerList = markerViewModel.getAllMarkers();
        Log.i("Info", "getMarkers: " + bMarkerList.size());
    }

    private void addMarkerListToMap(List<BMarker> bMarkerList) {
        List<BMarker> listForDisplay = bMarkerList;
        String marker_title, code, type;
        LatLng latLng;
        if (listForDisplay.isEmpty()) {
            listForDisplay = markerViewModel.getAllMarkers();
        }
        mMap.clear();
        for (BMarker BMarker : listForDisplay) {
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
//    private void showAllMarkers() {
//        if (!bMarkerList.isEmpty()) {
//            LatLng latLng;
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            int padding = 100;
//            String code;
//
//            for (BMarker BMarker : bMarkerList) {
//                latLng = new LatLng(BMarker.getLat(), BMarker.getLng());
//                builder.include(latLng);
//                code = BMarker.getCode();
//            }
//
//            LatLngBounds bounds =
//                    builder.build();
//            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
//            if (bMarkerList.size() != 1) {
//                // mMap.resetMinMaxZoomPreference();
//            }
//        }
//    }

    void syncChangedData() {
        String jsonStr = convertToJSON();
        Log.i(TAG, "JSON: : " + jsonStr);
        upload(jsonStr);
    }

    private void upload(String jsonStr) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://android.alexsykes.com/uploadMarkerData.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                // request body goes here

                String requestBody = jsonStr;
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        Log.d("string", stringRequest.toString());
        requestQueue.add(stringRequest);
    }

    String convertToJSON() {
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(this);
        MarkerDao markerDao = db.dao();

        // Get changed data
        List<BMarker> list = markerDao.uploadChangesToServer();

        // Then conver to json
        ArrayList<ArrayList<String>> markers = new ArrayList<ArrayList<String>>();
        for (BMarker m : list) {

            ArrayList<String> marker = new ArrayList();
            marker.add(String.valueOf(m.getMarker_id()));
            marker.add(String.valueOf(m.getLat()));
            marker.add(String.valueOf(m.getLng()));
//            marker.add(m.getType());
//            marker.add(m.getCode());
//            marker.add(m.getName());
            marker.add(String.valueOf(m.isNew()));
            marker.add(String.valueOf(m.isUpdated()));
            markers.add(marker);
        }
        JSONArray markerJSONArray = new JSONArray(markers);
        return markerJSONArray.toString();
    }

    public void showDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        MarkerDetailFragment newFragment = new MarkerDetailFragment();

//        if (isLargeLayout) {
//            // The device is using a large layout, so show the fragment as a dialog
//            newFragment.show(fragmentManager, "dialog");
//        } else {
        // The device is smaller, so show the fragment fullscreen
        Log.i(TAG, "showDialog: ");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        newFragment.show(fragmentManager, "Dialo");
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
//        }
    }


    private void addDataToDb(String response) {

        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(getApplicationContext());
        ParentDao parentDao = db.pdao();
        MarkerDao markerDao = db.dao();
        WaterDao waterDao = db.wdao();
        Parent parent;
        Water water;
        BMarker BMarker;

        JSONArray waters = new JSONArray();
        JSONArray markers = new JSONArray();
        JSONArray parents = new JSONArray();

        try {
            JSONArray jsonArray = new JSONArray(response);

            Log.i("Info", "JSONArray: " + jsonArray.length());
            waters = jsonArray.getJSONArray(0);
            Log.i("Info", "Waters: " + waters.length());
            markers = jsonArray.getJSONArray(1);
            Log.i("Info", "Markers: " + markers.length());
            parents = jsonArray.getJSONArray(2);
            Log.i("Info", "Parents: " + parents.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int index = 0; index < waters.length(); index++) {
            try {
                JSONObject theWater = new JSONObject(waters.get(index).toString());
                String name = theWater.getString("name");
                String type = theWater.getString("type");
                int parent_id = theWater.getInt("parent_id");
                int water_id = theWater.getInt("id");

                water = new Water(water_id, name, type, parent_id);
                waterDao.insertWater(water);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int index = 0; index < parents.length(); index++) {
            try {
                JSONObject parentO = new JSONObject(parents.get(index).toString());
                String name = parentO.getString("name");
                String type = parentO.getString("type");
                int id = parentO.getInt("id");

                parent = new Parent(id, name, type);
                parentDao.insertParent(parent);

//                Log.i("Info", "Parent: " +  name + " " + type + " " + id);

            } catch (JSONException e) {
                Log.i("Info", "Error ");
                e.printStackTrace();
            }
        }

        for (int index = 0; index < markers.length(); index++) {
            try {
                JSONObject m = new JSONObject(markers.get(index).toString());
                String name = m.getString("name");
                String code = m.getString("code");
                String type = m.getString("type");
                int water_id = m.getInt("water_id");
                int marker_id = m.getInt("id");
                double lat = m.getDouble("latitude");
                double lng = m.getDouble("longitude");

                BMarker = new BMarker(marker_id, name, code, type, water_id, lat, lng);
                markerDao.insertMarker(BMarker);

            } catch (JSONException e) {
                Log.i("Info", "Error ");
                e.printStackTrace();
            }
        }

        mapFragment.getMapAsync(this);
    }

    private void getSavedData() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.getBaseContext());
        String url = "https://android.alexsykes.com/getDataFromServer.php";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        addDataToDb(response);
                        // Log.i("Info", "onResponse: " + response.substring(0,150));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Info", "That didn't work!");

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    protected boolean canConnect() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}