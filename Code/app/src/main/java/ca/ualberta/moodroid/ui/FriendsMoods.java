package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Freezable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.auth.User;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.UserService;

public class FriendsMoods extends AppCompatActivity {

    UserService users;
    MoodEventService moodEvents;
    private static final int ACTIVITY_NUM=2;
    Intent intent;

//    public FriendsMoods(UserService userService, MoodEventService moodEventService) {
 //       this.users = userService;
    //      this.moodEvents = moodEventService;
 //   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_moods);
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
                        break;
                    case R.id.ic_moods:
                        intent = new Intent(FriendsMoods.this, MoodHistory.class);
                        startActivity(intent);
                        break;
                    case R.id.ic_notif:
                        intent = new Intent(FriendsMoods.this, Notifications.class);
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
