package com.alexsykes.bankmonsterr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.Marker;
import com.alexsykes.bankmonsterr.data.MarkerViewModel;
import com.alexsykes.bankmonsterr.utility.MarkerListAdapter;

import java.util.List;

public class WaterDetailActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "REPLY";

    private EditText editWaterView;
    int water_id;
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
        Log.i("Info", "water_id: " + water_id);
        getMarkers();
    }

    void getMarkers() {
        markerList = markerViewModel.getMarkerList(water_id);
//        Log.i("Info", "getMarkers: " + markerList.size());

        RecyclerView rv = findViewById(R.id.markerRecyclerView);
        final MarkerListAdapter adapter = new MarkerListAdapter(markerList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}