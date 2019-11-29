package ca.ualberta.moodroid.service;

import android.content.Context;

import com.google.android.gms.tasks.Task;

/**
 * The interface Authentication interface.
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
     * @param context
     * @return
     */
    public Task<Void> logOut(Context context);

}
