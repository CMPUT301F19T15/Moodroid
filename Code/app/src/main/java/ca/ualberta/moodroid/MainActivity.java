package ca.ualberta.moodroid;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import ca.ualberta.moodroid.ui.BottomNavView;
import ca.ualberta.moodroid.ui.FriendsMoods;
import ca.ualberta.moodroid.ui.MoodHistory;
import ca.ualberta.moodroid.ui.Notifications;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView();
     //   FirebaseFirestore db = FirebaseFirestore.getInstance();

    }
    // Bottom Navigation view for each activity
    private void bottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomnav);
     //   BottomNavView.setupBottomNavView(bottomNavigationViewEx);

        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.ic_friends:
                        intent = new Intent(MainActivity.this,  FriendsMoods.class);
                        startActivity(intent);
                    case R.id.ic_moods:
                        intent = new Intent(MainActivity.this, MoodHistory.class);
                        startActivity(intent);
                        break;
                    case R.id.ic_notif:
                        intent = new Intent(MainActivity.this, Notifications.class);
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
