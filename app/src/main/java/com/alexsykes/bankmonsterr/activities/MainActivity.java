package com.alexsykes.bankmonsterr.activities;

// https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
// https://normanaspx.medium.com/android-room-how-works-one-to-many-relationship-example-e8a17531a3bb


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.WaterAndParents;
import com.alexsykes.bankmonsterr.data.WaterViewModel;
import com.alexsykes.bankmonsterr.utility.WaterListAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static final int NEW_WATER_ACTIVITY_REQUEST_CODE = 1;
    BottomNavigationView bottomNavigationView;
    boolean canConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        canConnect = canConnect();
        if(canConnect) {
            Log.i("Info", "Can connect");
            getSavedData();
        } else {
            // showDialog();
            Log.i("Info", "Cannot connect");
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WaterListAdapter adapter = new WaterListAdapter(new WaterListAdapter.WaterDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        WaterViewModel waterViewModel = new ViewModelProvider(this).get(WaterViewModel.class);
        LiveData<List<WaterAndParents>> waterandparents = waterViewModel.getWaterAndParentList();
        waterandparents.observe(this, adapter::submitList);
    }

    private void getSavedData() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplication().getBaseContext());
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

    private void addDataToDb(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);

            Log.i("Info", "JSONArray: " + jsonArray.length());
            JSONArray waters = jsonArray.getJSONArray(0);
            Log.i("Info", "Waters: " + waters.length());
            JSONArray markers = jsonArray.getJSONArray(1);
            Log.i("Info", "Markers: " + markers.length());
            JSONArray parents = jsonArray.getJSONArray(2);
            Log.i("Info", "Parents: " + parents.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClickCalled(int id, String water_name) {
        // Check for connectivity
            Intent intent = new Intent(MainActivity.this, WaterDetailActivity.class);
            intent.putExtra("water_id", id);
            intent.putExtra("water_name",water_name);
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
                goAllWaters();
                return true;
        }
        return false;
    }



    private void goSettings() {
        Log.i("Info", "goSettings: ");

    }
    private void goAllWaters() {
        Intent intent = new Intent(MainActivity.this,AllWatersActivity.class );
        startActivity(intent);
    }

    private void goRivers() {
        Log.i("Info", "goRivers: ");
    }


    protected boolean canConnect() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}