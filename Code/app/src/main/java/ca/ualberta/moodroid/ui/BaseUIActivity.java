package ca.ualberta.moodroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.service.MoodEventService;

/**
 * This class sets up our apps navigation capability. It creates the bottom tool bar for the main
 * 4 activities in our app which are MoodHistory, FriendsMoods, Notifications, and Profile.
 */
public class BaseUIActivity extends AppCompatActivity {

    /**
     * The Activity num.
     */
    protected int ACTIVITY_NUM;


    /**
     * The Tool bar button left
     */
    @BindView(R.id.toolbar_button_left)
    ImageButton toolBarButtonLeft;

    /**
     * The Tool bar button right.
     */
    @BindView(R.id.toolbar_button_right)
    ImageButton toolBarButtonRight;

    /**
     * The text view that displays the title of the activity.
     */
    @BindView(R.id.toolbar_text_center)
    TextView toolBarTextView;


    /**
     * Sets title.
     *
     * @param title the title
     */
    protected void setTitle(String title) {
        toolBarTextView.setText(title);
    }

    /**
     * Gets nav id.
     *
     * @return the nav id
     */
    protected int getNavId() {
        return this.ACTIVITY_NUM;
    }

    /**
     * Gets me.
     *
     * @return the me
     */
    protected AppCompatActivity getMe() {
        return this;
    }


    /**
     * Setup the bottom navigation bar view and navigation
     */
    protected void bottomNavigationView() {
        //set up bottom navigation bar...go to corresponding activity
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomnav);
        final AppCompatActivity me = this.getMe();
        Menu menu = bottomNavigationViewEx.getMenu();
        Log.d("NAV", "Current Nav ID: " + this.getNavId());
        MenuItem menuItem = menu.getItem(this.getNavId());
        menuItem.setChecked(true);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_notif:
                        startActivity(new Intent(me, Notifications.class));
                        break;
                    case R.id.ic_moods:
                        startActivity(new Intent(me, MoodHistory.class));
                        break;
                    case R.id.ic_friends:
                        startActivity(new Intent(me, FriendsMoods.class));
                        break;
                    case R.id.ic_profile:
                        startActivity(new Intent(me, Profile.class));
                        break;
                }
                return false;
            }
        });
    }

}
