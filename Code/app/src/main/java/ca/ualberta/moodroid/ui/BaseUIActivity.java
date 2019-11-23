package ca.ualberta.moodroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.MoodEventService;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

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

    protected BottomNavigationViewEx bottomNavigationViewEx;

    protected Badge badge;


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
    protected void bottomNavigationView(int pageId) {
        //set up bottom navigation bar...go to corresponding activity
        bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomnav);
        final AppCompatActivity me = this.getMe();
        Menu menu = bottomNavigationViewEx.getMenu();

        FirebaseFirestore.getInstance().collection("followRequest").whereEqualTo("requesteeUsername", AuthenticationService.getInstance().getUsername()).whereEqualTo("state", FollowRequestModel.REQUESTED_STATE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("NOTIFICATION/COUNT", e.getMessage());
                }
                Log.d("NOTIFICATION/UPDATE", "new data. Count=" + queryDocumentSnapshots.size());
                if (queryDocumentSnapshots.size() == 0) {
                    if (badge != null) {
                        badge.hide(true);
                    }
                } else {
                    if (badge != null) {
                        badge.hide(false);
                    }
                    setNotificationCount(queryDocumentSnapshots.size());
                }

            }
        });

        MenuItem menuItem = menu.getItem(pageId);
        menuItem.setChecked(true);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_notif:
                        startActivity(new Intent(me, Notifications.class));
                        return true;
                    case R.id.ic_moods:
                        startActivity(new Intent(me, MoodHistory.class));
                        return true;
                    case R.id.ic_friends:
                        startActivity(new Intent(me, FriendsMoods.class));
                        return true;
                    case R.id.ic_profile:
                        startActivity(new Intent(me, Profile.class));
                        return true;
                }
                return false;
            }
        });
    }

    protected void setNotificationCount(int count) {
        if (badge == null) {
            badge = new QBadgeView(this)
                    .setBadgeNumber(count)
                    .setGravityOffset(12, 2, true)
                    .bindTarget(bottomNavigationViewEx.getBottomNavigationItemView(0));
        } else {
            badge.setBadgeNumber(count);
        }
    }

}
