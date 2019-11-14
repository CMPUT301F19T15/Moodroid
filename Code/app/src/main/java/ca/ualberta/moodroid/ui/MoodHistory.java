package ca.ualberta.moodroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;

//import ca.ualberta.moodroid.model.ModelInterface;
//import ca.ualberta.moodroid.model.MoodEventModel;
//import ca.ualberta.moodroid.model.MoodModel;


/**
 * Get the mood history for a logged in user
 */
public class MoodHistory extends BaseUIActivity implements MoodListAdapter.OnListListener {

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

    String[] spinnerMoodNames;
    String[] spinnerEmojis;
    Spinner spinner;
    CustomHistorySpinnerAdapter spinnerAdapter;
    private boolean isInteracting;


//    @Override
//    public void onUserInteraction() {
//        super.onUserInteraction();
//        isInteracting = true;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        moodEvents = new MoodEventService();
        moods = new MoodService();
        ButterKnife.bind(this);

        //Bottom Navigation Bar Listener
        bottomNavigationView();
        setTitle("Mood History");

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

//        toolBarButtonRight.setBackground(R.layout.history_filter_spinner);
//        toolBarButtonRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /////////////////////////////////////////////////
//                /////////////////////////////////////////////////
//                ////////////////////////////////////////////////
//            }
//        });


        //Recycler List View with all mood events of the user
        moodList = new ArrayList<>();
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


















        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> arrayListMoodNames = new ArrayList<>();
        ArrayList<String> arrayListEmojis = new ArrayList<>();
        //first item in each list should be blank to indicate no filter selected
        arrayListMoodNames.add("");
        arrayListEmojis.add("");



        moods.getAllMoods().addOnSuccessListener(new OnSuccessListener<List<MoodModel>>() {
            @Override
            public void onSuccess(List<MoodModel> moodModels) {
                allMoods = moodModels;
                for(MoodModel mood : allMoods){
                    arrayListMoodNames.add(mood.getName());
                    arrayListEmojis.add(mood.getEmoji());
                }
                spinnerEmojis = arrayListEmojis.toArray(new String[0]);
                spinnerMoodNames = arrayListMoodNames.toArray(new String[0]);
                spinnerAdapter = new CustomHistorySpinnerAdapter(MoodHistory.this, spinnerEmojis, spinnerMoodNames);
                spinner.setAdapter(spinnerAdapter);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isInteracting) {
//                    Toast.makeText(MoodHistory.this, spinnerEmojis[i], Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });


////////////////////on user interaction at top




















    }





    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isInteracting = true;
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
        openEditDeleteDialog();
    }

    /**
     * this is to start the new edit deletefragment
     * where the user can decide on what they want to do with
     * the list item that has been clicked
     */
    public void openEditDeleteDialog() {

        EditDeleteFragment editDeleteFragment = new EditDeleteFragment();
        editDeleteFragment.show(getSupportFragmentManager(), "Options");
    }
}
