package ca.ualberta.moodroid.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import ca.ualberta.moodroid.R;

public class AddLocation extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "Addlocation";
    GoogleMap mMap;
    SupportMapFragment mapFragment;
    SearchView searchView;

    /**
     * best provider
     */
    String bestProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_location);

        searchView = findViewById(R.id.sv_location);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.add_location_map);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mMap.clear();
                String location = searchView.getQuery().toString();

                List<Address> addressList = null;

                if(location != null || !location.equals("")){

                    Geocoder geocoder = new Geocoder(AddLocation.this);

                    try{

                        addressList = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e) {

                        e.printStackTrace();

                    }

                    Address address = addressList.get(0);

                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //setting the googlemap to mMap
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // set user location true to show on the map
        mMap.setMyLocationEnabled(true);

        // disable the button on the top right that takes you to your location
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        // create a new location manager to also get the Lat and Long of your location
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

        if( locationManager != null){
            // find the location and save it
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if( location != null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // create a new LatLng variable
                LatLng latLng = new LatLng(latitude,longitude);

                // set the new LatLng variable to the camera view
                setCameraView(latLng);
            }
        }
    }

    /**
     * this is where we set the initial camera view
     *
     * TO DO
     * - make the initial camera view the current location of user
     */
    private void setCameraView(LatLng latLng){

        // Set a boundary to start
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(latLng);

        // changed the zoom of the camera
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(4);

        // cant do both at once so we have to move camera first
        // then change the camera zoom
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

}
