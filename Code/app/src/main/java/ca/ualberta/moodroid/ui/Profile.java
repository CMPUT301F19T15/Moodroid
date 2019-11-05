package ca.ualberta.moodroid.ui;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import ca.ualberta.moodroid.MainActivity;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.ValidationService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * This shows the user's profile info, an option to navigate to the
 * map view, and an option to logout of the app.
 */


public class Profile extends AppCompatActivity {
    MoodEventService moodEvents;
    ValidationService validation;

    private static final int ACTIVITY_NUM = 3;
    private Intent intent;

    String myUserName;
    TextView userNameView;
    Button logOutButton;
    ImageButton toolBarButtonLeft;
    ImageButton toolBarButtonRight;
    TextView toolBarTextView;
    String toolBarText;
    // public Profile(MoodEventService moodEventService, ValidationService validationService) {
    //     this.moodEvents = moodEventService;

    //     this.validation = validationService;
    //     }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userNameView = findViewById(R.id.profile_user_name_text_view);
        logOutButton = findViewById(R.id.logout_button);
        toolBarButtonLeft = findViewById(R.id.toolbar_button_left);
        toolBarButtonRight = findViewById(R.id.toolbar_button_right);
        toolBarTextView = findViewById(R.id.toolbar_text_center);
        toolBarText = "Profile";

        //call to bottom navigation bar listener
        bottomNavigationView();

        //initialize the top navigation bar with the correct text and icons
        toolBarTextView.setText(toolBarText);
        toolBarButtonRight.setImageResource(R.drawable.ic_menu_map_foreground);
        toolBarButtonLeft.setVisibility(View.INVISIBLE);
        myUserName = AuthenticationService.getInstance().getUsername();
        userNameView.setText(myUserName);

        /**
         * When the user clicks the logout button, this finishes all open activities,
         * logs the user out and goes back to the initial login screen.
         */
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(Profile.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //finish all open activities & go to Sign In Screen (MainActivity)
                               finish();
                                Intent intent = new Intent(Profile.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
            }
        });

        /**
         * When the user clicks the map icon, this will start the MoodMap activity.
         */
        toolBarButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to MoodMap Activity
                intent = new Intent(Profile.this, MoodMap.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This sets up the bottom navigation bar functionality and sends the user to
     * the requested activity once the corresponding icon is clicked.
     */
    private void bottomNavigationView() {
        //set up bottom navigation bar...go to corresponding activity
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomnav);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_friends:
                        intent = new Intent(Profile.this, FriendsMoods.class);
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




