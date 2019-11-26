package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.icu.util.Freezable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.auth.User;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.UserService;

/**
 * This activity displays a list of mood events identical to MoodHistory except it shows the users
 * friends moods (users that have accepted your request or have been accepted by the user). The
 * moods are all displayed with their unique attributes.
 */
public class FriendsMoods extends MoodHistory {

    /**
     * the user service item that represents the users who have moods displayed.
     */
    UserService users;
    /**
     * The mood event items that display the details of the moods from said users.
     */
    MoodEventService moodEvents;
    private int ACTIVITY_NUM = 2;
    /**
     * An intent item, this item starts the AddFriend activity which is only reachable from this
     * activity.
     */
    Intent intent;
    /**
     * This is an array that holds mood events, which is whats displayed on the screen via an
     * adapter.
     */
    ArrayList<MoodEventModel> events;

    int currentUsers = 0;

    /**
     * The progress bar.
     */
    private ProgressBar progressBar;

    /**
     * Get all the users you follow, and get a list of each users moods - then sort and display the moods
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_moods);
        users = new UserService();
        moodEvents = new MoodEventService();
        allMoods = new ArrayList<>();
        events = new ArrayList<>();


        

        //set progress bar to visible until listview is ready to display items
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

     
        bottomNavigationView(ACTIVITY_NUM);
        ButterKnife.bind(this);
        this.setTitle("Friends Mood");


        moods.getAllMoods().addOnSuccessListener(new OnSuccessListener<List<MoodModel>>() {
            @Override
            public void onSuccess(List<MoodModel> moodModels) {
                Log.d("FRIENDSMOOD/MOOD", "Got moods, count=" + moodModels.size());
                allMoods = moodModels;
            }
        });


        users.getAllUsersIFollow().addOnSuccessListener(new OnSuccessListener<List<FollowRequestModel>>() {
            @Override
            public void onSuccess(List<FollowRequestModel> followRequestModels) {
                ArrayList<Task<List<MoodEventModel>>> taskList = new ArrayList<>();
                final int totalUsers = followRequestModels.size();
                for (FollowRequestModel user : followRequestModels) {
                    // TODO: I need to reinitiate this service otherwise it won't get all the objects.

                    Log.d("FRIENDSMOOD/FRIEND", "Got friend: " + user.getRequesteeUsername());
                    moodEvents.getEventsForUser(user.getRequesteeUsername()).addOnSuccessListener(new OnSuccessListener<List<MoodEventModel>>() {
                        @Override
                        public void onSuccess(List<MoodEventModel> moodEventModels) {
                            currentUsers += 1;
                            Log.d("FRIENDSMOOD/TASK", "Task completed for: " + user.getRequesteeUsername() + ", size=" + moodEventModels.size());
                            // only get the latest one
                            reverseSort(moodEventModels);
                            events.add(moodEventModels.get(0));
                            if (currentUsers >= totalUsers) {
                                Log.d("FRIENDSMOOD/USERCOUNT", currentUsers + "");
                                Log.d("FRIENDSMOOD/SIZE", events.size() + "");
                                reverseSort();
                                updateListView();
                            }
                        }
                    });
                }
            }
        });


//        toolBarButtonRight.setImageResource(R.drawable.ic_menu_map_foreground);
        toolBarButtonLeft.setImageResource(R.drawable.ic_person_add_black_24dp);
        toolBarButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to MoodMap Activity
                intent = new Intent(FriendsMoods.this, AddFriend.class);
                startActivity(intent);
            }
        });
        toolBarButtonRight.setImageResource(R.drawable.ic_menu_map_foreground);
        toolBarButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to MoodMap Activity
                intent = new Intent(FriendsMoods.this, FriendMap.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void reverseSort() {
        this.reverseSort(this.events);
    }

    protected void reverseSort(List<MoodEventModel> events) {
        Collections.sort(events, new Comparator<MoodEventModel>() {
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
     * Updates the internal list view items with a new listing of events
     */
    @Override
    protected void updateListView() {
        if (allMoods == null) {
            return;
        }
        moodListRecyclerView = findViewById(R.id.mood_list_view);
        moodListRecyclerView.setHasFixedSize(true);
        moodListLayoutManager = new LinearLayoutManager(FriendsMoods.this);
        moodListAdapter = new MoodListAdapter(events, allMoods, true, FriendsMoods.this);
        moodListRecyclerView.setLayoutManager(moodListLayoutManager);
        moodListRecyclerView.setAdapter(moodListAdapter);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onShortClick(int position) {
        if (events.size() != 0) {  //else, if click too fast: size = 0 and app crashes
            MoodEventModel moodEventModel = events.get(position);
            moodEventModel.getInternalId();
            intent = new Intent(FriendsMoods.this, ViewMoodDetail.class);
            intent.putExtra("eventId", moodEventModel.getInternalId());
            intent.putExtra("caller", FriendsMoods.class.toString());
            startActivity(intent);

        }
    }


    @Override
    public void onResume(){
        super.onResume();
        bottomNavigationView(ACTIVITY_NUM);

    }



}
