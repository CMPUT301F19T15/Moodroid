package ca.ualberta.moodroid.service;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import javax.inject.Inject;

import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.repository.UserRepository;

/**
 * Singleton pattern to get a user's username
 */
public class AuthenticationService implements AuthenticationInterface {

    /**
     *  The initial value for the authentication service instance
     */
    private static AuthenticationService service = null;

    /**
     *  String data that stores the users username
     */
    private String username;


    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static synchronized AuthenticationService getInstance() {
        if (service == null) {
            service = new AuthenticationService();
        }

        return service;
    }

    /**
     * Simple setter method that sets the username for the user
     *
     * @param name the name
     */

    public void setUsername(String name) {
        this.username = name;
    }

    /**
     * Simple getter method that fetches the username
     *
     * @return
     */

    public String getUsername() {
        return this.username;
    }

}


