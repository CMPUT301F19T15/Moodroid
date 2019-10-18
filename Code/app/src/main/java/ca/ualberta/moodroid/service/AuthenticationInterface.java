package ca.ualberta.moodroid.service;

public interface AuthenticationInterface {

    /**
     * Login a non-logged in user
     *
     * @return
     */
    public boolean login();

    /**
     * Check if the current person is logged in
     *
     * @return
     */
    public boolean isLoggedIn();


    /**
     * Check to see if the current user has a profile
     *
     * @return
     */
    public boolean isNewUser();


}
