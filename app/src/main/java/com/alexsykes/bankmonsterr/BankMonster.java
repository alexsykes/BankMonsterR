package com.alexsykes.bankmonsterr;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.alexsykes.bankmonsterr.data.BMarker;
import com.alexsykes.bankmonsterr.data.MarkerDao;
import com.alexsykes.bankmonsterr.data.Parent;
import com.alexsykes.bankmonsterr.data.ParentDao;
import com.alexsykes.bankmonsterr.data.Water;
import com.alexsykes.bankmonsterr.data.WaterDao;
import com.alexsykes.bankmonsterr.data.WaterRoomDatabase;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BankMonster extends Application {
    boolean canConnect;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Info", "onCreateLaunch: ");

        canConnect = canConnect();
        if(canConnect) {
            Log.i("Info", "Can connect");
            getSavedData();
        } else {
            Log.i("Info", "Cannot connect");
        }
        Log.i("Info", "onCreateLaunch: done ");
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

        for(int index = 0; index < waters.length(); index ++) {
            try {
                JSONObject theWater = new JSONObject(waters.get(index).toString());
                String name = theWater.getString("name");
                String type = theWater.getString("type");
                int parent_id = theWater.getInt("parent_id");
                int water_id = theWater.getInt("id");

                water = new Water(water_id,name,type,parent_id);
                waterDao.insertWater(water);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for(int index = 0; index < parents.length(); index ++) {
            try {
                JSONObject parentO = new JSONObject(parents.get(index).toString());
                String name = parentO.getString("name");
                String type = parentO.getString("type");
                int id = parentO.getInt("id");

                parent = new Parent(id,name,type);
                parentDao.insertParent(parent);

//                Log.i("Info", "Parent: " +  name + " " + type + " " + id);

            } catch (JSONException e) {
                Log.i("Info", "Error ");
                e.printStackTrace();
            }
        }

        for(int index = 0; index < markers.length(); index ++) {
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
