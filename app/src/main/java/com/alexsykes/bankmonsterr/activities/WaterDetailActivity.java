package com.alexsykes.bankmonsterr.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.Marker;
import com.alexsykes.bankmonsterr.data.MarkerViewModel;

import java.util.List;

public class WaterDetailActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "REPLY";

    private EditText editWaterView;
    int water_id;
    String water_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_detail);

        Intent intent = getIntent();
        water_id = intent.getIntExtra("water_id",-999);
        water_name = intent.getStringExtra("water_name");
        setTitle(water_name);
        Log.i("Info", "water_id: " + water_id);
        getMarkers();
    }

    void getMarkers() {
        MarkerViewModel markerViewModel = new ViewModelProvider(this).get(MarkerViewModel.class);
        markerViewModel.setWater_id(water_id);
        LiveData<List<Marker>> markerListForWater = markerViewModel.getMarkerListForWater(water_id);
        List<Marker> markerList = markerViewModel.getMarkerList(water_id);
    }
}