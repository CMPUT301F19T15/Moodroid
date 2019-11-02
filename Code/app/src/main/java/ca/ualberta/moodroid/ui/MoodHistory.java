package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;

import java.util.List;

import ca.ualberta.moodroid.R;
//import ca.ualberta.moodroid.model.ModelInterface;
//import ca.ualberta.moodroid.model.MoodEventModel;
//import ca.ualberta.moodroid.model.MoodModel;

import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.repository.MoodRepository;

import ca.ualberta.moodroid.service.MoodEventService;


public class MoodHistory extends AppCompatActivity implements MoodListAdapter.OnListListener {

    MoodRepository moods;
    MoodEventService moodEvents;
    private static final int ACTIVITY_NUM=1;
    private Intent intent;
    private RecyclerView moodListRecyclerView;
    private RecyclerView.Adapter moodListAdapter;
    private RecyclerView.LayoutManager moodListLayoutManager; //aligns items in list
    ArrayList<MoodEventModel> moodList;
    ImageButton toolBarButtonLeft;
    ImageButton toolBarButtonRight;
    TextView toolBarTextView;
    String toolBarText;

//    ////TO DO: fix this.
//    public MoodHistory(MoodEventService moodEventService) {
//        this.moodEvents = moodEventService;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        //Bottom Navigation Bar Listener
        bottomNavigationView();

        //set top toolbar text & buttons
        toolBarButtonLeft = findViewById(R.id.toolbar_button_left);                  //////you can just change the images for the buttons here....
        toolBarButtonRight = findViewById(R.id.toolbar_button_right);                /////and when you do the mood map you can do the same thing, just change it to different images
        toolBarTextView = findViewById(R.id.toolbar_text_center);                    //// if you don't a button to show, you can call toolBarButtonLeft.setVisibility(View.INVISIBLE);
        toolBarText = "Mood History";                                                ////
        toolBarTextView.setText(toolBarText);

        //TO DO: add on click listeners for toolbar


        //Recycler List View with all mood events of the user
        moodList = new ArrayList<>();
        List moods = moodEvents.getMyEvents(); //gets list of all user events
        moodList.addAll(moods);
        reverseSort();         //sort array in reverse order
        moodListRecyclerView = findViewById(R.id.mood_list_view);
        moodListRecyclerView.setHasFixedSize(true);
        moodListLayoutManager = new LinearLayoutManager(this);
        moodListAdapter = new MoodListAdapter(moodList, moodEvents, this);
        moodListRecyclerView.setLayoutManager(moodListLayoutManager);
        moodListRecyclerView.setAdapter(moodListAdapter);


    }

    private void reverseSort(){
        //sort array on date/time in reverse order
        Collections.sort(moodList, new Comparator<MoodEventModel>() {
            @Override
            public int compare(MoodEventModel mood1, MoodEventModel mood2) {
                return mood2.getDatetime().compareTo(mood1.getDatetime());      //reversed
            }
        });
    }

    private void bottomNavigationView(){
        //set up bottom navigation bar...go to corresponding activity
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomnav);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.ic_friends:
                        intent = new Intent(MoodHistory.this,  FriendsMoods.class);
                        startActivity(intent);
                        break;
                    case R.id.ic_moods:
                        //already in moods
                        break;
                    case R.id.ic_notif:
                        intent = new Intent(MoodHistory.this, Notifications.class);
                        startActivity(intent);
                        break;
                    case R.id.ic_profile:
                        //don't have profile activity yet
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onListClick(int position) {
        openEditDeleteDialog();
    }
    public void openEditDeleteDialog(){

        EditDeleteFragment editDeleteFragment = new EditDeleteFragment();
        editDeleteFragment.show(getSupportFragmentManager(),"Options");
    }
}
