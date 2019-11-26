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
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import butterknife.ButterKnife;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.repository.MoodRepository;
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
 */
public class MoodHistory extends BaseUIActivity implements MoodListAdapter.OnListListener {

    private static final String TAG = "MoodHistory";
    /**
     * this activity is just to display the users mood history
     * there is a toolbar which contains two buttons
     * one button for adding a new mood event
     * another for filtering the mood list by a certain mood
     */
// variables needed
    MoodEventService moodEvents;
    /**
     * The Moods.
     */
    MoodService moods;
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
     * boolean for our gps
     */
    private boolean mLocationPermissionGranted = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        moodEvents = new MoodEventService();
        moods = new MoodService();
        ButterKnife.bind(this);

        //Bottom Navigation Bar Listener
        bottomNavigationView(1);
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
                for(MoodModel mood : spinnerMoods){
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
                    if(i == 0){
                        filterMood = null;                      //first item = no filter
                    } else{
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
    private void getMood(){

        //Recycler List View with all mood events of the user
        moodList = new ArrayList<>();

        if(filterMood == null){
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
     * This indicates whether the user has interacted with the dropdown menu.
     */
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isInteracting = true;
    }

    /**
     * check the map services
     * @return
     */
    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
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
     * @return
     */
    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    /**
     * ask for location permission
     *
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
     * @return
     */
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MoodHistory.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MoodHistory.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * the result
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
                if(mLocationPermissionGranted){
                    getMood();
                }
                else{
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
        MoodEventModel moodEventModel = moodList.get(position);
        //gets the mood event by id
        String id = moodEventModel.getInternalId();
        MoodEventService moodEvent = new MoodEventService();
        moodEvent.getEventWithId(id).addOnSuccessListener(new OnSuccessListener<MoodEventModel>() {
            @Override
            public void onSuccess(MoodEventModel moodEventModel) {
                //For some reason internalid is set to null
                moodEventModel.setInternalId(id);
                String mood = moodEventModel.getMoodName();
                Intent intent = new Intent(MoodHistory.this, EditMoodDetail.class);
                intent.putExtra("eventId", moodEventModel.getInternalId());

                //gets mood details to pass in
                MoodRepository moodRepository = new MoodRepository();
                moodRepository.where("name", mood).get().addOnSuccessListener(new OnSuccessListener<List<ModelInterface>>() {
                    @Override
                    public void onSuccess(List<ModelInterface> modelInterfaces) {
                        MoodModel moodModel = (MoodModel) modelInterfaces.get(0);
                        intent.putExtra("emoji", moodModel.getEmoji());
                        intent.putExtra("mood_name", moodModel.getName());
                        intent.putExtra("hex", moodModel.getColor());
                        startActivity(intent);
                    }
                });

            }
        });
        //openEditDeleteDialog(position);
    }

    @Override
    public void onShortClick(int position) {
        if(moodList.size() != 0) {  //else, if click too fast: size = 0 and app crashes
            MoodEventModel moodEventModel = moodList.get(position);
            moodEventModel.getInternalId();
            intent = new Intent(MoodHistory.this, ViewMoodDetail.class);
            intent.putExtra("eventId", moodEventModel.getInternalId());
            startActivity(intent);

        }
    }


    /**
     * this is to start the new edit deletefragment
     * where the user can decide on what they want to do with
     * the list item that has been clicked
     */
    public void openEditDeleteDialog(int position) {

        MoodEventModel moodEvent = moodList.get(position);
        //Intent intent = new Intent(MoodHistory.this, EditDeleteFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("eventId", moodEvent.getInternalId());

        //create fragment
        EditDeleteFragment editDeleteFragment = new EditDeleteFragment();
        editDeleteFragment.setArguments(bundle);
        editDeleteFragment.show(getSupportFragmentManager(), "Options");
    }


    /**
     * on resume get moods
     */
    @Override
    protected void onResume(){
        super.onResume();
        if(checkMapServices()){
            if(mLocationPermissionGranted){
                getMood();
            }
            else{
                getLocationPermission();
            }
        }
    }
}
