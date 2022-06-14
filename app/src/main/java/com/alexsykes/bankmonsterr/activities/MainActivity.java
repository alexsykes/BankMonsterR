package com.alexsykes.bankmonsterr.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.Water;
import com.alexsykes.bankmonsterr.data.WaterViewModel;
import com.alexsykes.bankmonsterr.utility.WaterListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private WaterViewModel waterViewModel;
    public static final int NEW_WATER_ACTIVITY_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WaterListAdapter adapter = new WaterListAdapter(new WaterListAdapter.WaterDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        waterViewModel = new ViewModelProvider(this).get(WaterViewModel.class);
        waterViewModel.getAllWaters().observe(this, waters -> {
            adapter.submitList(waters);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, NewWaterActivity.class);
            startActivityForResult(intent, NEW_WATER_ACTIVITY_REQUEST_CODE);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WATER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Water word = new Water(data.getStringExtra(NewWaterActivity.EXTRA_REPLY));
            waterViewModel.insert(word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}