package ca.ualberta.moodroid.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

import ca.ualberta.moodroid.R;

/**
 * This class creates the main location attributes like the users location, the google map, and
 * sets up  the UI for the location tasks that are implemented. This file is the main java
 * source for the users mood locations that are represented on the map
 */

public class AddLocation extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "Addlocation";
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private SearchView searchView;

    /**
     * get exact user location
     */
    private FusedLocationProviderClient mFusedLocationProviderClient;
    /**
     * get places
     */
    private PlacesClient placesClient;
    /**
     * list of places from google
     */
    private List<AutocompletePrediction> predictionList;

    /**
     * get last known location
     */
    private Location mLastKnownLocation;
    /**
     * update user location
     */
    private LocationCallback locationCallback;

    /**
     * create a map view
     */
    private View mapView;

    /**
     * create a add location button
     */
    protected Button addLocation;

    /**
     * set default zoom
     */
    private final float DEFAULT_ZOOM = 15;

    /**
     * on create it will setup the map and setup the users location on the map
     * and center the map on the users location
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set content view to the layout
        setContentView(R.layout.activity_add_location);

        // connect the add button to the add button on the layout
        addLocation = findViewById(R.id.addLocationBtn);

        // create a text view for the emoji
        TextView emoji = (TextView) findViewById(R.id.emoji);


        // Below takes the intent from add_mood.java and displays the emoji, color and
        // mood title in the banner based off what the user chooses in that activity

        Intent intent = getIntent();

        String emojii = intent.getExtras().getString("emoji");
        String mood_name = intent.getExtras().getString("mood_name");
        String hex = intent.getExtras().getString("hex");

        emoji.setText(emojii);

        /**
         * setup the support fragment manager
         * and mapasync
         */
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.add_location_map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        /**
         * initialize mfusedLocationprovider client
         */
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AddLocation.this);

        /**
         * initialize the places
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCSp4zdtDsi7z0JeJGMEarMQXx_W-6iLZs");
        }

        /**
         * when user clicks on add location
         * it saves the centre location where the emoji is
         * saves the lat and lon and puts it into a intent
         * and goes back to the other activity and close this activity
         */
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latlng = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
                Log.d("addlocation", "this is the lat " + latlng.latitude);
                Log.d("addlocation", "this is the long " + latlng.longitude);
                Intent intent = new Intent();
                String lat = String.valueOf(latlng.latitude);
                String lon = String.valueOf(latlng.longitude);
                //String latLng = String.valueOf(latlng);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                //intent.putExtra("latlng", latLng);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }

    /**
     * on map ready
     * it creates the map with the style
     * and checks if location has been granted
     * @param googleMap
     */
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

        // check that location is enabled on the users phone
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // set user location true to show on the map
        mMap.setMyLocationEnabled(true);

        // disable the button on the top right that takes you to your location
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // move location button to place
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // set to bottop
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        // check for gps
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // request to see current location builder
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        // check settings for the location on users phone
        SettingsClient settingsClient = LocationServices.getSettingsClient(AddLocation.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        // if everything works it will get the device location
        task.addOnSuccessListener(AddLocation.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        // sends user to settings to enable location if not allowed
        task.addOnFailureListener(AddLocation.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(AddLocation.this, 51);
                    } catch (IntentSender.SendIntentException el) {
                        el.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * check if device has location enabled
     * if its enabled then get device location
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }

    }


    /**
     * getting device latest known location
     * and set camera view to the current location
     * so the emoji is set on the users location
     */
    private void getDeviceLocation() {

        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            // check if null
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        // move camera to the current location
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(AddLocation.this, "unable to get location", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    /**
     * this is where we set the initial camera view
     * <p>
     * TO DO
     * - make the initial camera view the current location of user
     */
    private void setCameraView(LatLng latLng) {

        // Set a boundary to start
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(latLng);

        // changed the zoom of the camera
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(4);

        // cant do both at once so we have to move camera first
        // then change the camera zoom
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

}
