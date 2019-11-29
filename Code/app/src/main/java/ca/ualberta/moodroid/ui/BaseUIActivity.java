package ca.ualberta.moodroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import javax.inject.Inject;

import butterknife.BindView;
import ca.ualberta.moodroid.ContextGrabber;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.UserService;
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

    /**
     * The Auth.
     */
    @Inject
    AuthenticationService auth;

    /**
     * The Users.
     */
    @Inject
    UserService users;

    /**
     * The Bottom navigation view ex.
     */
    protected BottomNavigationViewEx bottomNavigationViewEx;

    /**
     * The Badge.
     */
    protected Badge badge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


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
     * This sets the bottom nav bar that is used in our 4 main activities.
     *
     * @param pageID the page id
     */
    protected void bottomNavigationView(int pageID) {
        ContextGrabber.get().di().inject(BaseUIActivity.this);

        //set up bottom navigation bar...go to corresponding activity
        bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomnav);
        final AppCompatActivity me = this.getMe();
        Menu menu = bottomNavigationViewEx.getMenu();

        /**
         * the block below updates the nav bar visually if there are any notifications that
         * have come in for the user to check
         */
        users.getFollowRequestsReference(FollowRequestModel.REQUESTED_STATE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("NOTIFICATION/COUNT", e.getMessage());
                }
                if (queryDocumentSnapshots != null) {
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

            }
        });


        /**
         * This block below is the main logic for the nav bars function, it uses a switch
         * that detects the tap from the user on any of the main 4 pages, and switches to
         * the correct activity, each icon on the nav bar reflects the correct page the app
         * is displaying, and the bar also functions correctly visually if the user uses the back
         * button.
         */
        MenuItem menuItem = menu.getItem(pageID);
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

    /**
     * Sets notification count.
     *
     * @param count the count
     */
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
