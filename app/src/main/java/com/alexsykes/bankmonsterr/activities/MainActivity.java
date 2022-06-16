package com.alexsykes.bankmonsterr.activities;

// https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
// https://normanaspx.medium.com/android-room-how-works-one-to-many-relationship-example-e8a17531a3bb


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.Water;
import com.alexsykes.bankmonsterr.data.WaterViewModel;
import com.alexsykes.bankmonsterr.utility.WaterListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private WaterViewModel waterViewModel;
    public static final int NEW_WATER_ACTIVITY_REQUEST_CODE = 1;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WaterListAdapter adapter = new WaterListAdapter(new WaterListAdapter.WaterDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        waterViewModel = new ViewModelProvider(this).get(WaterViewModel.class);
        waterViewModel.getAllWaters().observe(this, waters -> {
            adapter.submitList(waters);
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.bottom_nav_bar, menu);
//        return true;
//    }

    public void onClickCalled(int id) {
        // Check for connectivity
            Intent intent = new Intent(MainActivity.this, NewWaterActivity.class);
            intent.putExtra("water_id", id);
            startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu_item:
                goSettings();
                return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goSettings:
                goSettings();
                return true;
            case R.id.goRivers:
                goRivers();
                return true;
            case R.id.goWaters:
                goWaters();
                return true;
        }
        return false;
    }



    private void goSettings() {
        Log.i("Info", "goSettings: ");

    }
    private void goWaters() {
        Log.i("Info", "goWaters: ");
    }

    private void goRivers() {
        Log.i("Info", "goRivers: ");
    }
/*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WATER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
           // Water word = new Water(data.getStringExtra(NewWaterActivity.EXTRA_REPLY));
          //  waterViewModel.insert(word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }*/
}