package ca.ualberta.moodroid.ui;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.ualberta.moodroid.MainActivity;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.ValidationService;

/**
 * The type Profile.
 */
public class Profile extends BaseUIActivity {
    /**
     * This shows the user's profile info, an option to navigate to the
     * map view, and an option to logout of the app.
     */

    MoodEventService moodEvents;
    /**
     * The Validation.
     */
    ValidationService validation;

    private int ACTIVITY_NUM = 3;
    private Intent intent;

    /**
     * The My user name.
     */
    String myUserName;

    /**
     * The User name view.
     */
    @BindView(R.id.profile_user_name_text_view)
    TextView userNameView;
    /**
     * The Log out button.
     */
    @BindView(R.id.logout_button)
    Button logOutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        //call to bottom navigation bar listener
        bottomNavigationView();
        setTitle("Profile");

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
                intent = new Intent(Profile.this, Map.class);
                startActivity(intent);
            }
        });
    }

}




