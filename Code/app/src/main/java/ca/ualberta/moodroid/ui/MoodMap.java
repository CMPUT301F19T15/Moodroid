package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.GeolocationService;
import ca.ualberta.moodroid.service.MoodEventService;

public class MoodMap extends AppCompatActivity implements OnMapReadyCallback {
    public MoodMap() {

    }

    private static final String TAG = "Maps activity";
    MoodEventService moodEvents;
    GeolocationService geolocation;

    private GoogleMap mMap;
    ImageButton toolBarButtonLeft;
    ImageButton toolBarButtonRight;
    TextView toolBarTextView;
    String toolBarText;
    private Intent intent;

    public MoodMap(MoodEventService moodEventService, GeolocationService geolocationService) {
        //this.moodEvents = moodEventService;
        //this.geolocation = geolocationService;
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
     * we just add a marker near Sydney, Australia.
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

        // Add a marker in Sydney and move the camera
        LatLng edmonton = new LatLng(53.631611,-113.323975);
        mMap.addMarker(new MarkerOptions().position(edmonton).title("EDMONTON MARKER"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(edmonton));
    }
}
