package ca.ualberta.moodroid.ui;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ClusterMarker;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.service.GeolocationService;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.util.MyClusterManagerRenderer;

public class MoodMap extends AppCompatActivity implements OnMapReadyCallback {
    public MoodMap() {

    }

    private static final String TAG = "Maps activity";
    MoodEventService moodEvents;
    GeolocationService geolocation;

    private MapView mMapView;
    private GoogleMap mMap;
    ImageButton toolBarButtonLeft;
    ImageButton toolBarButtonRight;
    TextView toolBarTextView;
    String toolBarText;
    private Intent intent;

    //mock
    private Marker mEdmonton;
    private LatLngBounds mMapBoundary;
    private ArrayList<MoodEventModel> myMoods = new ArrayList<>();
    //
    private ClusterManager<ClusterMarker> mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();



    public MoodMap(MoodEventService moodEventService, GeolocationService geolocationService) {
        this.moodEvents = moodEventService;
        this.geolocation = geolocationService;
    }

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

        // do i add this to get the list of all my moods then check if it has a location attached to it?
        //mood.get().addOnSuccessListener(new OnSuccessListener<List<ModelInterface>>() {
        //    @Override
        //    public void onSuccess(List<ModelInterface> modelInterfaces) {
        //        for (ModelInterface m : modelInterfaces) {
        //            MoodModel s = (MoodModel) m;
        //            Log.d("RESULT/GET", s.getInternalId() + s.getName());
        //        }
        //    }
        //});



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

        /**
        mClusterManager = new ClusterManager<>(this, mMap);

        mClusterManagerRenderer = new MyClusterManagerRenderer(
                this,
                mMap,
                mClusterManager
        );

        int avatar = R.drawable.mad;

        ClusterMarker newClusterMarker = new ClusterMarker(
                new LatLng(53.631611, -113.323975),
                "Bryce",
                "test",
                avatar);
        mClusterManager.addItem(newClusterMarker);
        mClusterMarkers.add(newClusterMarker);

        ClusterMarker newClustrMarker = new ClusterMarker(
                new LatLng(55.631611, -110.323975),
                "Bryce",
                "test",
                avatar);
        mClusterManager.addItem(newClustrMarker);
        mClusterMarkers.add(newClustrMarker);

        mClusterManager.cluster();**/

        //addMapMarkers();
    }

    private void addMapMarkers(){

        LatLng edmonton = new LatLng(53.631611,-113.323975);
        mEdmonton = mMap.addMarker(new MarkerOptions()
                .position(edmonton)
                .title("Sydney")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mad)));

        /**
        // Add a marker in Edmonton and move the camera
        LatLng edmonton = new LatLng(53.631611,-113.323975);
        mMap.addMarker(new MarkerOptions().position(edmonton).title("EDMONTON MARKER"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(edmonton));
        **/


        /**
        if(mMap != null) {
            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<>(this, mMap);
            }
            if (mClusterManagerRenderer == null) {
                mClusterManagerRenderer = new MyClusterManagerRenderer(
                        this,
                        mMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }

            String snippet = "";
            snippet = "Angry";
            int avatar = R.drawable.mad;
            ClusterMarker newClusterMarker = new ClusterMarker(
                    new LatLng(53.631611, -113.323975),
                    "Bryce",
                    snippet,
                    avatar
            );
            mClusterManager.addItem(newClusterMarker);
            mClusterMarkers.add(newClusterMarker);


            mClusterManager.cluster();
            setCameraView();
        }
        /**

         **/

    }


    private void setCameraView(){
        // Set a boundary to start
        double bottomBoundary = 53.523749 - .1;
        double leftBoundary = -113.526234 - .1;
        double topBoundary = 53.523749 + .1;
        double rightBoundary = -113.526234 + .1;

        mMapBoundary = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary, 0));
    }

}
