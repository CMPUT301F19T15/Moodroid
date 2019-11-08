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
import ca.ualberta.moodroid.service.GeolocationService;
import ca.ualberta.moodroid.service.MoodEventService;

/**
 * This class creates a map that displays moods via their corresponding emoji in the location
 * that the mood event was created, if the user chooses to do so.
 */
public class MoodMap extends FragmentActivity implements OnMapReadyCallback {
    /**
     * Instantiates a new Mood map.
     */
    public MoodMap() {

    }

    private static final String TAG = "Maps activity";
    /**
     * The Mood events.
     */
    MoodEventService moodEvents;
    /**
     * The Geolocation. Helps pinpoint the current location on the map.
     */
    GeolocationService geolocation;

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
     * Instantiates a new Mood map.
     *
     * @param moodEventService   the mood event service
     * @param geolocationService the geolocation service
     */
    public MoodMap(MoodEventService moodEventService, GeolocationService geolocationService) {
        this.moodEvents = moodEventService;
        this.geolocation = geolocationService;
    }

    /**
     * New instance mood map.
     *
     * @return the mood map
     */
    public static MoodMap newInstance() {
        return new MoodMap();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mood_maps);

        // get all the ids for the tool bar
        toolBarButtonLeft = findViewById(R.id.toolbar_button_left);
        toolBarButtonRight = findViewById(R.id.toolbar_button_right);
        toolBarTextView = findViewById(R.id.toolbar_text_center);
        toolBarText = "My Moods";

        // set the tool bar to the certain pictures and texts
        toolBarTextView.setText(toolBarText);
        toolBarButtonLeft.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolBarButtonRight.setImageResource(R.drawable.ic_compare_arrows_black_24dp);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // on click listener for the back button to go back to the profile page
        toolBarButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to back to profile activity
                intent = new Intent(MoodMap.this, Profile.class);
                startActivity(intent);
            }
        });

        // on click listener for the compare button to compare our moods to friends moods
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
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Edmonton.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
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

        addMapMarkers();
        setCameraView();

    }

    private void addMapMarkers(){

        final IconGenerator iconFactory = new IconGenerator(this);
        MoodEventRepository moodEvents = new MoodEventRepository();
        // get all model interfaces (moodeventModel) then change it to a moodeventModel and get the location
        moodEvents.where("username", "nguy").get().addOnSuccessListener(new OnSuccessListener<List<ModelInterface>>() {
            @Override
            public void onSuccess(List<ModelInterface> modelInterfaces) {
                for(ModelInterface m: modelInterfaces) {
                    MoodEventModel event = (MoodEventModel) m;
                    Log.d("MARKER", "NEW EVENT LOCATION: "+event.getLocation());
                    if(event.getMoodName().equals("Mad")) {
                        addIcon(iconFactory, "\uD83D\uDE21", new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()));
                    }
                    if(event.getMoodName().equals("Sad")) {
                        addIcon(iconFactory, "\uD83D\uDE2D", new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()));
                    }
                    addIcon(iconFactory, "\uD83D\uDE2D", new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()));
                }
            }
        });


    }

    private void addIcon(IconGenerator iconFactory, String text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position);

        mMap.addMarker(markerOptions);
    }


    private void setCameraView(){
        // Set a boundary to start

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(53.525687, -113.125607));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(7);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

}
