package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.TableLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ca.ualberta.moodroid.MainActivity;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.repository.UserRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;

import static androidx.appcompat.app.AlertDialog.*;


public class MoodHistory extends AppCompatActivity {

    MoodEventService moodEvents;
    private static final int ACTIVITY_NUM=1;
    private Intent intent;
    private RecyclerView moodListRecyclerView;
    private RecyclerView.Adapter moodListAdapter;
    private RecyclerView.LayoutManager moodListLayoutManager; //aligns items in list
    ArrayList<MoodEventModel> moodList;

     public MoodHistory(MoodEventService moodEventService) {
        this.moodEvents = moodEventService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        //Bottom Navigation Bar Listener
        bottomNavigationView();

        //Recycler List View with all mood events of the user
        moodList = new ArrayList<>();
        List moods = moodEvents.getMyEvents(); //gets list of all user events
        moodList.addAll(moods);
        reverseSort();         //sort array in reverse order
        moodListRecyclerView = findViewById(R.id.mood_list_view);
        moodListRecyclerView.setHasFixedSize(true);
        moodListLayoutManager = new LinearLayoutManager(this);
        moodListAdapter = new MoodListAdapter(moodList, moodEvents);
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

}
