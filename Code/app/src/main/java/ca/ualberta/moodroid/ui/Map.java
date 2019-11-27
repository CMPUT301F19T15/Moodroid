package ca.ualberta.moodroid.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.ui.IconGenerator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.ualberta.moodroid.ContextGrabber;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;

/**
 * The type Map.
 */
public class Map extends FragmentActivity implements OnMapReadyCallback {


    @Inject
    AuthenticationService auth;


    @Inject
    MoodService moodService;


    @Inject
    MoodEventService moodEventService;

    /**
     * Map activity where it creates the map with the
     * desired markers on the map and sets the camera view
     * <p>
     * TO DO
     * - Make the marker look better and easier to see
     * - add icon generator to do that
     * - change the map view to current location
     * - implement the current location on the map
     * - add an on long click listener to the marker
     * to take the user to view all of the mood details for that mood
     * - if time then add clusters
     */

    public Map() {
    }

    /**
     * setting the tag
     */
    private static final String TAG = "Maps activity";


    /**
     * any variable that are needed
     */
    public GoogleMap mMap;
    /**
     * The Tool bar button left.
     */
    ImageButton toolBarButtonLeft;
    /**
     * The Tool bar button right.
     */
    ImageButton toolBarButtonRight;
    /**
     * The Tool bar text view.
     */
    TextView toolBarTextView;
    /**
     * The Tool bar text.
     */
    String toolBarText;
    protected Intent intent;

    /**
     * The My user name.
     */
    String myUserName;

    /**
     * best provider
     */
    String bestProvider;


    List<MoodModel> moods;


    /**
     * New instance map.
     *
     * @return the map
     */
    public static Map newInstance() {
        return new Map();
    }

    /**
     * when the user clicks on the
     * map icon it will create
     * all the buttons
     * and any text views
     * that need to be changed
     * <p>
     * also sets the on click listeners
     * for each button
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextGrabber.get().di().inject(Map.this);

        // gets the username of the user and saves it to the string to use later
        myUserName = auth.getUsername();


        // setting the view to the mood maps view
        setContentView(R.layout.activity_my_mood_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // get all the ids for the tool bar
        toolBarButtonLeft = findViewById(R.id.toolbar_button_left);
        toolBarButtonRight = findViewById(R.id.toolbar_button_right);
        toolBarTextView = findViewById(R.id.toolbar_text_center);
        toolBarText = "My Moods";

        // set the tool bar to the certain pictures and texts
        toolBarTextView.setText(toolBarText);
        toolBarButtonLeft.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolBarButtonRight.setImageResource(R.drawable.ic_compare_arrows_black_24dp);


        /**
         * when the user clicks on the tool bar left button it will
         * navigate the user back to their profile page
         */
        toolBarButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to back to profile activity
                intent = new Intent(Map.this, Profile.class);
                startActivity(intent);
            }
        });

        /**
         *
         * when the user clicks on the tool bar right button it will
         * add all of their friends moods to the map
         * or navigate to another page where it has all
         * of the users moods and their friends moods
         *
         * need to decide on whats best
         *
         */
        toolBarButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to the map with all my moods and all friends moods
                //
                //
                // ADD HERE
                //
                //
            }
        });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines,
     * add listeners or move the camera. In this case,
     * we just add a marker near Edmonton.
     * <p>
     * If Google Play services is not installed on the device,
     * the user will be prompted to install
     * it inside the SupportMapFragment.
     * This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * <p>
     * Creates the map with the certain style of the map.
     * connects to the Map style json in raw
     * Can change the map style if needed just
     * need to change the mapstyle json
     * <p>
     * then calls the add map markers
     * then calls the set camera view
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

        if (locationManager != null) {
            // find the location and save it
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // create a new LatLng variable
                LatLng latLng = new LatLng(latitude, longitude);

                // set the new LatLng variable to the camera view
                setCameraView(latLng);
            }
        }

        moodService.getAllMoods().addOnSuccessListener(new OnSuccessListener<List<MoodModel>>() {
            @Override
            public void onSuccess(List<MoodModel> moodModels) {
                moods = moodModels;
                // call to add to map
                addMapMarkers();
            }
        });


    }

    /**
     * this is to add the map markers to the map
     * <p>
     * it creates an icon generator
     * <p>
     * pulls all of the Mood events for that user
     * <p>
     * loops through all of the event mood model
     * and then adds it by calling the addicon
     * <p>
     * each mood will have
     * - emoji
     * - location
     * - date time as title
     * - situation as snippit
     */
    protected void addMapMarkers() {

        // creating new icon generator
        final IconGenerator iconFactory = new IconGenerator(this);

        // get all model interfaces (moodeventModel) then change it to a moodeventModel and get the location
        moodEventService.getMyEvents().addOnSuccessListener(new OnSuccessListener<List<MoodEventModel>>() {
            @Override
            public void onSuccess(List<MoodEventModel> moodEventModels) {
                Log.d("MOODMAP/GETEVENTS", "Got mood Events: " + moodEventModels.size());
                mMap.clear();
                for(MoodEventModel eventModel : moodEventModels){
                    // setting the MoodEventModel to event so it then can be used to
                    // call the get location, emoji,....
                    MoodEventModel event = (MoodEventModel) eventModel;

                    // just making sure it actually works
                    Log.d("MARKER", "NEW EVENT LOCATION: " + event.getLocation());

                    // Need the try here because not every MoodEventModel will
                    // have a location and
                    // we only want the MoodEventModels with a location
                    // to show
                    try {
                        // This could have a possible race condition
                        for (MoodModel mood : moods) {
                            if (mood.getName().equals(event.getMoodName())) {
                                addIcon(iconFactory, mood.getEmoji(), new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()), event.getDatetime(), event.getSituation());
                            }
                        }

                        // just catching any locations that have null values for them
                    } catch (NullPointerException e) {
                        Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage());
                    }
                }
            }
        });


    }

    /**
     * this is to set the actual markers on the map
     * takes in the param below and adds it using
     * MarkerOptions
     * <p>
     * TO DO
     * - make it look better
     * - maybe add an icon generator class to do that
     *
     * @param iconFactory
     * @param text
     * @param position
     * @param dateTime
     * @param socialSit
     */
    protected void addIcon(IconGenerator iconFactory, String text, LatLng position, String dateTime, String socialSit) {

        // creates a new markerOptions with the desired title, location, snippit, emoji
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).title(dateTime).snippet(socialSit);

        // adding the marker to the map
        mMap.addMarker(markerOptions);
    }

    /**
     * this is where we set the initial camera view
     * <p>
     * TO DO
     * - make the initial camera view the current location of user
     */
    public void setCameraView(LatLng latLng) {

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
