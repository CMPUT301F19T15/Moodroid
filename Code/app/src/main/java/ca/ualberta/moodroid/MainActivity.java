package ca.ualberta.moodroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.UserModel;
import ca.ualberta.moodroid.repository.UserRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.UserService;
import ca.ualberta.moodroid.ui.MoodHistory;
import ca.ualberta.moodroid.ui.SignUp;

/**
 * The main activity of our app.
 */
@Singleton
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Inject
    UserRepository users;

    @Inject
    UserService userService;

    @Inject
    AuthenticationService auth;

    private Intent intent;

    /**
     * The Tool bar button left.
     */
    ImageButton toolBarButtonLeft;
    /**
     * The Tool bar button right.
     */
    ImageButton toolBarButtonRight;
    /**
     * The Tool bar text view.
     */
    TextView toolBarTextView;
    /**
     * The Tool bar text.
     */
    String toolBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContextGrabber.get().di().inject(MainActivity.this);


        Log.d("TIMESTAMP", "" + new Date().getTime());
        /**
         * If there exists a username in the shared preferences, lets just log the user in directly
         */
        String me = auth.getUsername();
        Log.d("MAIN/AUTH", "I am currently: " + me);
        if (me != null) {
            startActivity(new Intent(MainActivity.this, MoodHistory.class));
        } else {
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTheme(R.style.LoginTheme)
                            .setLogo(R.drawable.newlogo)
                            .build(),
                    123);
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("AUTH", "Sign-in Successful" + user.getDisplayName());

                userService.getUserById(user.getUid()).addOnSuccessListener(new OnSuccessListener<UserModel>() {
                    @Override
                    public void onSuccess(UserModel userModel) {
                        if (userModel != null) {
                            auth.setUsername(userModel.getUsername());
                            Log.d("AUTH", "User lookup Successful" + userModel.getUsername() + user.getUid());
                            startActivity(new Intent(MainActivity.this, MoodHistory.class));
                        } else {
                            startActivity(new Intent(MainActivity.this, SignUp.class));
                        }
                    }
                });
            } else {
                Log.d("AUTH", "Signin failed");
            }
        }
    }
}