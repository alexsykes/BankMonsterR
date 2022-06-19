package com.alexsykes.bankmonsterr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.Marker;
import com.alexsykes.bankmonsterr.data.MarkerViewModel;
import com.alexsykes.bankmonsterr.utility.MarkerListAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class WaterDetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    public static final String EXTRA_REPLY = "REPLY";

    private GoogleMap mMap;
    private EditText editWaterView;
    int water_id;
    int height, width;
    String water_name;
    MarkerViewModel markerViewModel;
    List<Marker> markerList;

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
        markerList = markerViewModel.getMarkerList(water_id);
        RecyclerView rv = findViewById(R.id.markerRecyclerView);
        final MarkerListAdapter adapter = new MarkerListAdapter(markerList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onMapLoaded() {
        //  mMap.resetMinMaxZoomPreference();
        Log.i("Info", "onMapLoaded: ");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
        mMap.setMaxZoomPreference(15);

//        height = findViewById(R.id.map).getHeight();
//        width = findViewById(R.id.map).getWidth();
        if (!markerList.isEmpty()) {
            LatLng latLng;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            int padding = 40;
            String code;

            for(Marker marker: markerList) {
                latLng = new LatLng(marker.getLat(), marker.getLng());
                builder.include(latLng);
                code = marker.getCode();
                mMap.addMarker(new MarkerOptions().position(latLng).title(code));
            }

            LatLngBounds bounds  =
                    builder.build();

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,width,height, padding));
        }
    }
}