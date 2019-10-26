package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.UserService;

public class Notifications extends AppCompatActivity {

    UserService users;
    private static final int ACTIVITY_NUM=0;
    private Intent intent;

    public Notifications(UserService userService) {
        this.users = userService;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
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
                        intent = new Intent(Notifications.this,  FriendsMoods.class);
                        startActivity(intent);
                        break;
                    case R.id.ic_moods:
                        intent = new Intent(Notifications.this, MoodHistory.class);
                        startActivity(intent);
                        break;
                    case R.id.ic_notif:
                        break;
                    case R.id.ic_profile:
                        break;
                }
                return false;
            }
        });
    }
}
