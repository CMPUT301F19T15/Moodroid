package ca.ualberta.moodroid.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ca.ualberta.moodroid.ContextGrabber;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;

import static ca.ualberta.moodroid.ui.Constants.ERROR_DIALOG_REQUEST;
import static ca.ualberta.moodroid.ui.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static ca.ualberta.moodroid.ui.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

//import ca.ualberta.moodroid.model.ModelInterface;
//import ca.ualberta.moodroid.model.MoodEventModel;
//import ca.ualberta.moodroid.model.MoodModel;


/**
 * Get the mood history for a logged in user
 * This is the main page the user will see after logging into the app. the user
 * will always be brought to this page first when the app is run.
 */
public class MoodHistory extends BaseUIActivity implements MoodListAdapter.OnListListener, EditDeleteFragment.OnInputListener {

    private static final String TAG = "MoodHistory";
    /**
     * this activity is just to display the users mood history
     * there is a toolbar which contains two buttons
     * one button for adding a new mood event
     * another for filtering the mood list by a certain mood
     */
    @Inject
    MoodEventService moodEvents;

    /**
     * The Moods.
     */
    @Inject
    MoodService moods;

    /**
     * The Auth.
     */
    @Inject
    AuthenticationService auth;

    private int ACTIVITY_NUM = 1;
    private Intent intent;
    /**
     * The Mood list recycler view.
     */
    protected RecyclerView moodListRecyclerView;
    /**
     * The Mood list adapter.
     */
    protected RecyclerView.Adapter moodListAdapter;
    /**
     * The Mood list layout manager.
     */
    protected RecyclerView.LayoutManager moodListLayoutManager; //aligns items in list
    /**
     * The Mood list.
     */
    ArrayList<MoodEventModel> moodList;
    /**
     * The All moods.
     */
    List<MoodModel> allMoods;

    /**
     * All mood models to be displayed in the drop down menu.
     */
    List<MoodModel> spinnerMoods;

    /**
     * Keeps all String moods names to be displayed in the drop down menu.
     */
    String[] spinnerMoodNames;

    /**
     * Keeps all emojis to be displayed in the drop down menu.
     */
    String[] spinnerEmojis;

    /**
     * The drop down menu.
     */
    Spinner spinner;

    /**
     * The custom spinner adapter.
     */
    CustomHistorySpinnerAdapter spinnerAdapter;

    /**
     * Boolean to indicate whether the user has interacted with the spinner.
     */
    private boolean isInteracting;

    /**
     * String holding the mood name to filter the list view by.
     */
    private String filterMood;

    /**
     * Boolean for our gps.
     */
    private boolean mLocationPermissionGranted = false;

    /**
     * The progress bar.
     */
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        ContextGrabber.get().di().inject(MoodHistory.this);
//        moodEvents = new MoodEventService();
        ButterKnife.bind(this);

        //set progress bar to visible until listview is ready to display items
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //Bottom Navigation Bar Listener
        bottomNavigationView(ACTIVITY_NUM);
        setTitle("Mood History");
        filterMood = null;

        /**
         * Initialize the top navigation bar buttons and set listeners for them.
         */
        toolBarButtonLeft.setImageResource(R.drawable.ic_addmood);
        toolBarButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to AddMood Activity
                intent = new Intent(MoodHistory.this, AddMood.class);
                startActivity(intent);
            }
        });

        /**
         * initialize the spinner (drop down menu)
         */
        toolBarButtonRight.setVisibility(View.GONE);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> arrayListMoodNames = new ArrayList<>();
        ArrayList<String> arrayListEmojis = new ArrayList<>();

        //set first item in list to No Filter
        arrayListMoodNames.add("No Filter");
        arrayListEmojis.add("     ");

        //populate the drop down menu with all available mood types + 1 blank
        moods.getAllMoods().addOnSuccessListener(new OnSuccessListener<List<MoodModel>>() {
            @Override
            public void onSuccess(List<MoodModel> moodModels) {
                spinnerMoods = moodModels;
                for (MoodModel mood : spinnerMoods) {
                    arrayListMoodNames.add(mood.getName());
                    arrayListEmojis.add(mood.getEmoji());
                }
                spinnerEmojis = arrayListEmojis.toArray(new String[0]);
                spinnerMoodNames = arrayListMoodNames.toArray(new String[0]);
                spinnerAdapter = new CustomHistorySpinnerAdapter(MoodHistory.this, spinnerEmojis, spinnerMoodNames);
                spinner.setAdapter(spinnerAdapter);
            }
        });


        /**
         * This will filter the list view to only show the events of the type the user selected in the
         * drop down menu.
         */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isInteracting) {
                    if (i == 0) {
                        filterMood = null;                      //first item = no filter
                    } else {
                        filterMood = spinnerMoodNames[i];
                    }
                    getMood();
                }
            }


            /**
             * This is a Callback method that is invoked when the selection disappears from this view.
             * @param adapterView
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                filterMood = null;
                getMood();
            }
        });


/**
 * This gets all mood events to be displayed and updates the list view.
 */
    }

    /**
     * Gets mood.
     */
    public void getMood() {


        //Recycler List View with all mood events of the user
        moodList = new ArrayList<>();

        if (filterMood == null) {
            moodEvents.getMyEvents().addOnSuccessListener(new OnSuccessListener<List<MoodEventModel>>() {
                @Override
                public void onSuccess(List<MoodEventModel> moodEventModels) {

                    Log.d("MOODHISTORY/GET", "Got mood Events: " + moodEventModels.size());

                    moods.getAllMoods().addOnSuccessListener(new OnSuccessListener<List<MoodModel>>() {
                        @Override
                        public void onSuccess(List<MoodModel> moodModels) {
                            moodList.addAll(moodEventModels);
                            allMoods = moodModels;
                            reverseSort();
                            updateListView();
                        }
                    });
                }
            });
        } else {
            moodEvents.getMyEvents(filterMood).addOnSuccessListener(new OnSuccessListener<List<MoodEventModel>>() {
                @Override
                public void onSuccess(List<MoodEventModel> moodEventModels) {

                    Log.d("MOODHISTORY/GET", "Got mood Events: " + moodEventModels.size());
                    moodList.addAll(moodEventModels);
                    reverseSort();
                    updateListView();
                }
            });
        }
    }

    /**
     * After the object is deleted in the fragment, lets update the display to show that its been deleted
     *
     * @param eventId
     */
    @Override
    public void deleteCallback(String eventId) {
        // https://stackoverflow.com/questions/8520808/how-to-remove-specific-object-from-arraylist-in-java
        moodList.removeIf(t -> t.getInternalId() == eventId);
        updateListView();
    }

    /**
     * This indicates whether the user has interacted with the dropdown menu.
     */
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isInteracting = true;
    }

    /**
     * check the map services
     *
     * @return
     */
    private boolean checkMapServices() {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * prompt user to accept to use gps
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to access all of its futures, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * see if gps is enabled for the device
     * if yes returns true
     * if no it will call message
     *
     * @return boolean
     */
    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    /**
     * ask for location permission
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            getMood();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * sees if device can use google services
     * if not able then it will prompt user to install
     * if useable it will return true
     *
     * @return boolean
     */
    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MoodHistory.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MoodHistory.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * the result
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    /**
     * continue with the app
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {
                    getMood();
                } else {
                    getLocationPermission();
                }
            }
        }
    }


    /**
     * reverse sorting all of the mood events
     */
    protected void reverseSort() {
        Collections.sort(moodList, new Comparator<MoodEventModel>() {
            @Override
            public int compare(MoodEventModel mood1, MoodEventModel mood2) {
                try {

                    return mood2.dateObject().compareTo(mood1.dateObject());      //reversed
                } catch (Exception e) {
                    Log.e("MOODHISTORY/SORT", "Could not sort mood history: " + e.getMessage());
                }
                return 0;
            }
        });
    }


    /**
     * update the list view
     */
    protected void updateListView() {
        moodListRecyclerView = findViewById(R.id.mood_list_view);
        moodListRecyclerView.setHasFixedSize(true);
        moodListLayoutManager = new LinearLayoutManager(MoodHistory.this);
        moodListAdapter = new MoodListAdapter(moodList, allMoods, false, MoodHistory.this);
        moodListRecyclerView.setLayoutManager(moodListLayoutManager);
        moodListRecyclerView.setAdapter(moodListAdapter);
        progressBar.setVisibility(View.GONE);
    }


    /**
     * checking when the user clicks on the list
     * takes the certain postion
     * and opens up that edit delete diaglog
     *
     * @param position
     */
    @Override
    public void onListClick(int position) {

        openEditDeleteDialog(position);
    }

    @Override
    public void onShortClick(int position) {
        if (moodList.size() != 0) {  //else, if click too fast: size = 0 and app crashes
            MoodEventModel moodEventModel = moodList.get(position);
            moodEventModel.getInternalId();

            intent = new Intent(MoodHistory.this, ViewMoodDetail.class);
            intent.putExtra("eventId", moodEventModel.getInternalId());
            intent.putExtra("caller", MoodHistory.class.toString());
            startActivity(intent);

        }
    }


    /**
     * this is to start the new edit deletefragment
     * where the user can decide on what they want to do with
     * the list item that has been clicked
     *
     * @param position the position
     */
    public void openEditDeleteDialog(int position) {

        MoodEventModel moodEvent = moodList.get(position);
        //Intent intent = new Intent(MoodHistory.this, EditDeleteFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("eventId", moodEvent.getInternalId());
        bundle.putString("mood_name", moodEvent.getMoodName());
        // TODO not DI
        MoodRepository moodRepository = new MoodRepository();
        moodRepository.where("name", moodEvent.getMoodName()).get().addOnSuccessListener(new OnSuccessListener<List<ModelInterface>>() {
            @Override
            public void onSuccess(List<ModelInterface> modelInterfaces) {
                for (ModelInterface m : modelInterfaces) {
                    MoodModel s = (MoodModel) m;
                    Log.d("RESULT/GET", s.getInternalId());
                    bundle.putString("emoji", s.getEmoji());
                    bundle.putString("hex", s.getColor());
                }
            }
        });


        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag("edit_delete_fragment");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        EditDeleteFragment editFrag = new EditDeleteFragment();
        editFrag.setArguments(bundle);
        editFrag.show(manager, "edit_delete_fragment");

    }


    /**
     * on resume get moods
     */
    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView(ACTIVITY_NUM);
        if (checkMapServices()) {
            if (mLocationPermissionGranted) {
                getMood();
            } else {
                getLocationPermission();
            }
        }
    }


}
