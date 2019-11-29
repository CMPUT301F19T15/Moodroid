package ca.ualberta.moodroid.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.ualberta.moodroid.ContextGrabber;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.repository.UserRepository;
import ca.ualberta.moodroid.ui.Profile;

/**
 * Singleton pattern to get a user's username
 */
@Singleton
public class AuthenticationService implements AuthenticationInterface {

    /**
     * The initial value for the authentication service instance
     */
    private static AuthenticationService service = null;

    /**
     * String data that stores the users username
     */
    private String username;


    /**
     * Gets instance.
     *
     * @return the instance
     */
    @Inject
    public AuthenticationService() {
    }

    /**
     * Simple setter method that sets the username for the user
     *
     * @param name the name
     */

    public void setUsername(String name) {

        SharedPreferences pref = this.getPreferences();
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("username", name);
        editor.commit();

        this.username = name;
    }

    /**
     * Simple getter method that fetches the username
     *
     * @return
     */

    public String getUsername() {
        // If the username is not set, lets try to grab it from the preferences
        if (this.username == null) {
            String savedUsername = this.getPreferences().getString("username", null);
            Log.d("AUTH/PREF", "Found preferences username set to: " + savedUsername);
            // If there is no value, then there is no username
            if (savedUsername == null) {
                return null;
            } else {
                // set the username if it was found in the preferences
                this.username = savedUsername;
            }
        }

        return this.username;
    }

    /**
     * Clears the username if logging out
     */
    public void clearUsername() {
        SharedPreferences.Editor editor = this.getPreferences().edit();
        editor.remove("username");
        editor.commit();
        this.username = null;
    }

    /**
     * Return the shared preferences object
     *
     * @return
     */
    private SharedPreferences getPreferences() {
        Log.d("AUTH/PREF", "" + ContextGrabber.get().getPackageName());
        return ContextGrabber.get().getSharedPreferences("ca.ualberta.moodroid.PREFERENCE_FILE", Context.MODE_PRIVATE);
    }


    /**
     * Log the user out.
     * @param context
     * @return
     */

    public Task<Void> logOut(Context context){
        return AuthUI.getInstance().signOut(context).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                clearUsername();
            }
        });
    }

}


