package ca.ualberta.moodroid.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.GeolocationService;
import ca.ualberta.moodroid.service.MoodEventService;

public abstract class Map extends FragmentActivity implements OnMapReadyCallback {
    private boolean mIsRestore;

    private static final String TAG = "Maps activity";

    private GoogleMap mMap;
    ImageButton toolBarButtonLeft;
    ImageButton toolBarButtonRight;
    TextView toolBarTextView;
    String toolBarText;
    private Intent intent;

    MoodEventService moodEvents;
    GeolocationService geolocation;

    /**
    public MoodMap(MoodEventService moodEventService, GeolocationService geolocationService) {
        this.moodEvents = moodEventService;
        this.geolocation = geolocationService;
    }
     **/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get all the ids for the tool bar
        toolBarButtonLeft = findViewById(R.id.toolbar_button_left);
        toolBarButtonRight = findViewById(R.id.toolbar_button_right);
        toolBarTextView = findViewById(R.id.toolbar_text_center);
        toolBarText = "My Moods";

        // set the tool bar to the certain pictures and texts
        toolBarTextView.setText(toolBarText);
        toolBarButtonLeft.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolBarButtonRight.setImageResource(R.drawable.ic_compare_arrows_black_24dp);

        // on click listener for the back button to go back to the profile page
        toolBarButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to back to profile activity
                intent = new Intent(Map.this, Profile.class);
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

        setContentView(getLayoutId());
        setUpMap();
    }

    protected int getLayoutId() {
        return R.layout.activity_my_mood_maps;
    }

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

        startMyMap(mIsRestore);

    }

    protected abstract void startMyMap(boolean isRestore);

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    protected GoogleMap getMap() {
        return mMap;
    }

}

