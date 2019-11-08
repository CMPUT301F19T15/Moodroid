package ca.ualberta.moodroid.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.service.AuthenticationService;

/**
 * The type Map.
 */
public class Map extends FragmentActivity implements OnMapReadyCallback {

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
    // setting tag
    private static final String TAG = "Maps activity";




    // any variable needed
    private GoogleMap mMap;
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
    private Intent intent;

    /**
     * The My user name.
     */
    String myUserName;


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
     *
     * also sets the on click listeners
     * for each button
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // gets the username of the user and saves it to the string to use later
        myUserName = AuthenticationService.getInstance().getUsername();

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
     *
     * If Google Play services is not installed on the device,
     * the user will be prompted to install
     * it inside the SupportMapFragment.
     * This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * Creates the map with the certain style of the map.
     * connects to the Map style json in raw
     * Can change the map style if needed just
     * need to change the mapstyle json
     *
     * then calls the add map markers
     * then calls the set camera view
     *
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

        // call to add to map
        addMapMarkers();

        // call to set camera view
        setCameraView();

    }

    /**
     * this is to add the map markers to the map
     *
     * it creates an icon generator
     *
     * pulls all of the Mood events for that user
     *
     * loops through all of the event mood model
     * and then adds it by calling the addicon
     *
     * each mood will have
     * - emoji
     * - location
     * - date time as title
     * - situation as snippit
     *
     */
    private void addMapMarkers(){

        // creating new icon generator
        final IconGenerator iconFactory = new IconGenerator(this);

        // creates new moodeventrepository to then be used to get all the even mood models
        MoodEventRepository moodEvents = new MoodEventRepository();

        // get all model interfaces (moodeventModel) then change it to a moodeventModel and get the location
        moodEvents.where("username", myUserName).get().addOnSuccessListener(new OnSuccessListener<List<ModelInterface>>() {

            @Override
            public void onSuccess(List<ModelInterface> modelInterfaces) {

                // for loop to loop through all of the MoodEventModels
                for(ModelInterface m: modelInterfaces) {

                    // setting the MoodEventModel to event so it then can be used to
                    // call the get location, emoji,....
                    MoodEventModel event = (MoodEventModel) m;

                    // just making sure it actually works
                    Log.d("MARKER", "NEW EVENT LOCATION: "+event.getLocation());

                    // Need the try here because not every MoodEventModel will
                    // have a location and
                    // we only want the MoodEventModels with a location
                    // to show
                    try {

                        // if mood name is mad set the icon
                        if (event.getMoodName().equals("Mad")) {
                            addIcon(iconFactory, "\uD83D\uDE21", new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()), event.getDatetime(), event.getSituation());
                        }

                        // if mood name is sad set the icon
                        if (event.getMoodName().equals("Sad")) {
                            addIcon(iconFactory, "\uD83D\uDE1E", new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()), event.getDatetime(), event.getSituation());
                        }

                        // if mood name is annoyed set the icon
                        if (event.getMoodName().equals("Annoyed")) {
                            addIcon(iconFactory, "\uD83D\uDE12", new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()), event.getDatetime(), event.getSituation());
                        }

                        // if mood name is sick set the icon
                        if (event.getMoodName().equals("Sick")) {
                            addIcon(iconFactory, "\uD83E\uDD2E", new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()), event.getDatetime(), event.getSituation());
                        }

                        // if mood name is happy set the icon
                        if (event.getMoodName().equals("Happy")) {
                            addIcon(iconFactory, "\uD83D\uDE0A", new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()), event.getDatetime(), event.getSituation());
                        }

                        // if mood name is scared set the icon
                        if (event.getMoodName().equals("Scared")) {
                            addIcon(iconFactory, "\uD83D\uDE31", new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()), event.getDatetime(), event.getSituation());
                        }

                     // just catching any locations that have null values for them
                    }catch (NullPointerException e){
                        Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage() );
                    }
                }
            }
        });


    }

    /**
     * this is to set the actual markers on the map
     * takes in the param below and adds it using
     * MarkerOptions
     *
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
    private void addIcon(IconGenerator iconFactory, String text, LatLng position, String dateTime, String socialSit) {

        // creates a new markerOptions with the desired title, location, snippit, emoji
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).title(dateTime).snippet(socialSit);

        // adding the marker to the map
        mMap.addMarker(markerOptions);
    }

    /**
     * this is where we set the initial camera view
     *
     * TO DO
     * - make the initial camera view the current location of user
     */
    private void setCameraView(){

        // Set a boundary to start
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(53.525687, -113.125607));

        // changed the zoom of the camera
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(7);

        // cant do both at once so we have to move camera first
        // then change the camera zoom
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

}