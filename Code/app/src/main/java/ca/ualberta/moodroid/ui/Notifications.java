package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.List;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.UserModel;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.UserService;

public class Notifications extends AppCompatActivity {

    FollowRequestRepository users;
    private static final int ACTIVITY_NUM = 0;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        users = new FollowRequestRepository();

        users.where("requesteeUsername", AuthenticationService.getInstance().getUsername()).where("state", "undecided").get().addOnCompleteListener(new OnCompleteListener<List<ModelInterface>>() {
            @Override
            public void onComplete(@NonNull Task<List<ModelInterface>> task) {
                if (task.isSuccessful()) {
                    //
                    for (ModelInterface m : task.getResult()) {
                        FollowRequestModel req = (FollowRequestModel) m;
                        Log.d("NOTIFICATIONS/REQUEST", "Found Request: " + req.getInternalId());
                    }
                } else {
                    Log.d("NOTIFICATIONS/REQUEST", "Couldn't find anything");
                }
            }
        });
    }

    private void bottomNavigationView() {

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomnav);
        //   BottomNavView.setupBottomNavView(bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_friends:
                        intent = new Intent(Notifications.this, FriendsMoods.class);
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
