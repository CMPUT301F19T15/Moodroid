package ca.ualberta.moodroid.ui;




import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import ca.ualberta.moodroid.MainActivity;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.GeolocationService;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;
import ca.ualberta.moodroid.service.ValidationService;


    public class Profile extends AppCompatActivity{
    MoodEventService moodEvents;
    ValidationService validation;

    private static final int ACTIVITY_NUM=3;
    private Intent intent;

   // public Profile(MoodEventService moodEventService, ValidationService validationService) {
   //     this.moodEvents = moodEventService;

   //     this.validation = validationService;
        //     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Bottom Navigation Bar Listener
        bottomNavigationView();

        ///////set the top nav bar to the correct text and icon once top bar is fixed
        ///////////////////////
        ///////////////////////
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
                            intent = new Intent(Profile.this,  FriendsMoods.class);
                            startActivity(intent);
                            break;
                        case R.id.ic_moods:
                            intent = new Intent(Profile.this, MoodHistory.class);
                            startActivity(intent);
                            break;
                        case R.id.ic_notif:
                            intent = new Intent(Profile.this, Notifications.class);
                            startActivity(intent);
                            break;
                        case R.id.ic_profile:
                           //already in profile activity
                            break;
                    }
                    return false;
                }
            });
        }




    }




