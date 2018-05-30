package com.example.android.shushme;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnRequestPermissionsResultCallback
{

    // Constants
    public static final String TAG = MainActivity.class.getSimpleName();

    // Member variables
    private PlaceListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    /**
     * Called when the activity is starting
     *
     * @param savedInstanceState The Bundle that contains the data supplied in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.places_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PlaceListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // DONE (4) Create a GoogleApiClient with the LocationServices API and GEO_DATA_API
        GoogleApiClient gclient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "GoogleAPI connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "GoogleAPI suspended.");
    }

    // DONE (5) Override onConnected, onConnectionSuspended and onConnectionFailed for GoogleApiClient

    @Override
    protected void onResume() {
        super.onResume();
        //CheckBox location_checkbox = (CheckBox) findViewById(R.id.lo
        //ActivityCompat.requestPermissions(this, );
        // DONE (7) Override onResume and inside it initialize the location permissions checkbox
        CheckBox locationPermissions = (CheckBox) findViewById(R.id.cb_location_permissions);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            locationPermissions.setChecked(false);
        } else {
            locationPermissions.setChecked(true);
            locationPermissions.setEnabled(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "GoogleAPI connection failed.");
    }

    // DONE (8) Implement onLocationPermissionClicked to handle the CheckBox click event
    public void onLocationPermissionsClick(View view)
    {
        if (view.getId() == R.id.cb_location_permissions)
        {
            if (((CheckBox)view).isChecked())
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},42);
                Log.d(TAG, "Checked!");
            } else {
                Log.d(TAG, "Not Checked!");
            }

        }
    }

    public void onAddPlacesButtonClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable location permissions first", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Location Permissions Granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grantResults) {

        Log.d(TAG, "Got results for permission request "+requestCode);

        if (requestCode != 42) {
            Log.d(TAG, "Unknown permission request code "+requestCode);
            return;
        }

        if (permissions.length == 0 && grantResults.length == 0) {
            Log.d(TAG, "Permissions dialog interrupted!");
            return;
        }

        if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "Got Permissions!");
        } else {
            Log.d(TAG, "Didn't get permissions:");
            for(int i = 0; i<permissions.length; i++) {
                Log.d(TAG, "\t"+permissions[i]+" - "+(grantResults[i]==PackageManager.PERMISSION_GRANTED? "Granted":"Denied"));
            }
        }

    }

    // done (9) Implement the Add Place Button click event to show  a toast message with the permission status


}
