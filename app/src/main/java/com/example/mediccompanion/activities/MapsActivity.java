package com.example.mediccompanion.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mediccompanion.PI;
import com.example.mediccompanion.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    SearchView searchView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //VARS
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        String apiKey = "AIzaSyC1orobUaNa2Wd6gWbtpMeKPDurY4GFu3o";
        Places.initialize(getApplicationContext(), apiKey);

        searchView = new SearchView(Objects.requireNonNull(getActionBar()).getThemedContext());
        searchView.setQueryHint("Search for Places…");
        searchView.setIconifiedByDefault(true);
        search();


    }
    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                List<Address> list = new ArrayList<>();

                try {
                    list = geocoder.getFromLocationName(s, 1);
                }catch (IOException e){
                    Toast.makeText(getBaseContext(), "IOEXCEPTION " +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (list.size() > 0){
                    Address address = list.get(0);
                    //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
                    moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),DEFAULT_ZOOM, address.getAddressLine(0)) ;
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getPermissions();

        if (mLocationPermissionsGranted){
            Toast.makeText(this, "Getting your location", Toast.LENGTH_SHORT).show();
            getDeviceLocation();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        String add = "";
                        if (addresses.size() > 0){
                            for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++){
                                add += addresses.get(0).getAddressLine(i);
                            }
                            Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (mLocationPermissionsGranted){
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        //Toast.makeText(MapsActivity.this, "Device Location found", Toast.LENGTH_SHORT).show();
                        Location currentLocation = (Location) task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), MapsActivity.DEFAULT_ZOOM);
                    }else {
                        Toast.makeText(MapsActivity.this, "Unable to find device", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void getPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        mLocationPermissionsGranted = false;
                        return;
                    }
                }
                mLocationPermissionsGranted = true;
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                //move map
                initMap();
            }
        }
    }

    private void initMap(){
        moveCameraOnLaunchApp();
    }

    private void moveCameraOnLaunchApp() {
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add("Search")
                .setIcon(R.drawable.search)
                .setActionView(searchView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_home){
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }
        if (id == R.id.action_history) {
            Intent intent = new Intent(this, History.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_settings){
            Intent i = new Intent(this, MedicationStock.class);
            startActivity(i);
            return true;
        }else if (id == R.id.action_about){
            Intent intent = new Intent(this, Help.class);
            startActivity(intent);
        }else if(id == R.id.action_help){
            Intent intent = new Intent(this, Help.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    private void moveCamera(LatLng latLng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        if (title != "My Location"){
            mMap.addMarker(markerOptions);
        }
    }
}
