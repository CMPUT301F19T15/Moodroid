package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Collection;

import ca.ualberta.moodroid.MainActivity;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;


public class MoodHistory extends AppCompatActivity {

    MoodEventService moodEvents;
    private static final int ACTIVITY_NUM=1;
    private Intent intent;
    private RecyclerView moodListRecyclerView;
    private RecyclerView.Adapter moodListAdapter;
    private RecyclerView.LayoutManager moodListLayoutManager; //aligns items in list

   // public MoodHistory(MoodEventService moodEventService) {
  //      this.moodEvents = moodEventService;
   // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        //Bottom Navigation Bar Listener
        bottomNavigationView();

        //Recycler List View
        ArrayList<DELEEEEEEEETEtest> moodList = new ArrayList<>();
        //----------------delete...to test only -----------------------------------------------------------------------------------------------------------------------------
        moodList.add(new DELEEEEEEEETEtest(android.R.drawable.ic_menu_report_image, "Angry", "11/10/2019", "10:15"));
        moodList.add(new DELEEEEEEEETEtest(android.R.drawable.ic_menu_report_image, "Happy", "11/10/2019", "10:15"));
        moodList.add(new DELEEEEEEEETEtest(android.R.drawable.ic_menu_report_image, "Sad", "11/10/2019", "10:15"));
        moodList.add(new DELEEEEEEEETEtest(android.R.drawable.ic_menu_report_image, "Angry", "11/10/2019", "10:15"));
        moodList.add(new DELEEEEEEEETEtest(android.R.drawable.ic_menu_report_image, "Happy", "11/10/2019", "10:15"));
        moodList.add(new DELEEEEEEEETEtest(android.R.drawable.ic_menu_report_image, "Sad", "11/10/2019", "10:15"));
        moodList.add(new DELEEEEEEEETEtest(android.R.drawable.ic_menu_report_image, "Angry", "11/10/2019", "10:15"));
        moodList.add(new DELEEEEEEEETEtest(android.R.drawable.ic_menu_report_image, "Happy", "11/10/2019", "10:15"));
        moodList.add(new DELEEEEEEEETEtest(android.R.drawable.ic_menu_report_image, "Sad", "11/10/2019", "10:15"));
        moodList.add(new DELEEEEEEEETEtest(android.R.drawable.ic_menu_report_image, "Angry", "11/10/2019", "10:15"));
        moodList.add(new DELEEEEEEEETEtest(android.R.drawable.ic_menu_report_image, "Happy", "11/10/2019", "10:15"));
        moodList.add(new DELEEEEEEEETEtest(android.R.drawable.ic_menu_report_image, "Sad", "11/10/2019", "10:15"));
        // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        moodListRecyclerView = findViewById(R.id.mood_list_view);
        moodListRecyclerView.setHasFixedSize(true);         //because we know size of the list view will not change (incr performance)
        moodListLayoutManager = new LinearLayoutManager(this);
        moodListAdapter = new MoodListAdapter(moodList);

        moodListRecyclerView.setLayoutManager(moodListLayoutManager);
        moodListRecyclerView.setAdapter(moodListAdapter);
    }


    private void bottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomnav);
        //   BottomNavView.setupBottomNavView(bottomNavigationViewEx);
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
                        break;
                    case R.id.ic_notif:
                        intent = new Intent(MoodHistory.this, Notifications.class);
                        startActivity(intent);
                        break;
                    case R.id.ic_profile:
                        break;
                }
                return false;
            }
        });
    }

}
