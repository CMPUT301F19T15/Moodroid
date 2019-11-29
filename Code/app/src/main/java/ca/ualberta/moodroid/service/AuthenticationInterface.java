package ca.ualberta.moodroid.service;

import android.content.Context;

import com.google.android.gms.tasks.Task;

/**
 * The interface Authentication interface.
 * implemented in AuthenticationService
 */
public interface AuthenticationInterface {

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername();

    /**
     * Sets username.
     *
     * @param name the name
     */
    public void setUsername(String name);

    /**
     * Log the user out.
     *
     * @param context the context
     * @return task
     */
    public Task<Void> logOut(Context context);

}
