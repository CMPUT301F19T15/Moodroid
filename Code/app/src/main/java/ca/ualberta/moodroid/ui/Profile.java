package ca.ualberta.moodroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.ualberta.moodroid.ContextGrabber;
import ca.ualberta.moodroid.MainActivity;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.AuthenticationService;

/**
 * This class creates the Profile activity, which can be navigated to from the main bottom
 * tool bar. The user can see their username on this screen and also logout from here. The user can
 * also access their mood map from this screen.
 */
public class Profile extends BaseUIActivity {


    private int ACTIVITY_NUM = 3;

    /**
     * create intent
     */
    private Intent intent;

    /**
     * A string containing the username.
     */
    String myUserName;

    /**
     * A text view containing the username.
     */
    @BindView(R.id.profile_user_name_text_view)
    TextView userNameView;
    /**
     * The Log out button.
     */
    @BindView(R.id.logout_button)
    Button logOutButton;

    /**
     * The Auth.
     */
    @Inject
    AuthenticationService auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        ContextGrabber.get().di().inject(Profile.this);

        //call to bottom navigation bar listener
        bottomNavigationView(ACTIVITY_NUM);
        setTitle("Profile");

        toolBarButtonRight.setImageResource(R.drawable.ic_menu_map_foreground);
        toolBarButtonLeft.setVisibility(View.INVISIBLE);
        myUserName = auth.getUsername();
        userNameView.setText(myUserName);

        /**
         * When the user clicks the logout button, this finishes all open activities,
         * logs the user out and goes back to the initial login screen.
         */
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.logOut(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
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


//                AuthUI.getInstance()
//                        .signOut(Profile.this)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                /**
//                                 * Ensure we clear the username so they really are logged out
//                                 */
//                                auth.clearUsername();
//                                //finish all open activities & go to Sign In Screen (MainActivity)
//                                finish();
//                                Intent intent = new Intent(Profile.this, MainActivity.class);
//                                startActivity(intent);
//                            }
//                        });
//            }
//        });

        /**
         * When the user clicks the map icon, this will start the MoodMap activity.
         */
        toolBarButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to MoodMap Activity
                intent = new Intent(Profile.this, Map.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavigationView(ACTIVITY_NUM);

    }


}




