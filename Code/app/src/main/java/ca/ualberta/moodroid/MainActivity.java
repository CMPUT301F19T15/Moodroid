package ca.ualberta.moodroid;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;

import ca.ualberta.moodroid.ui.BottomNavView;
import ca.ualberta.moodroid.ui.FriendsMoods;
import ca.ualberta.moodroid.ui.MoodHistory;
import ca.ualberta.moodroid.ui.Notifications;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


import javax.inject.Inject;
import javax.inject.Singleton;

import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.UserModel;
import ca.ualberta.moodroid.repository.UserRepository;
import ca.ualberta.moodroid.ui.Profile;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.ui.AddFriend;
import ca.ualberta.moodroid.ui.SignUp;

/**
 * The type Main activity.
 */
@Singleton
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private UserRepository users;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.users = new UserRepository();
        Log.d("TIMESTAMP", "" + new Date().getTime());
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                final UserRepository users = this.users;
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("AUTH", "Signin Successful" + user.getDisplayName());
                users.find(user.getUid()).addOnCompleteListener(new OnCompleteListener<ModelInterface>() {
                    @Override
                    public void onComplete(@NonNull Task<ModelInterface> task) {
                        UserModel m = (UserModel) task.getResult();
                        if (m != null) {
                            AuthenticationService.getInstance().setUsername(m.getUsername());
                            Log.d("AUTH", "User lookup Successful" + m.getUsername() + user.getUid());
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