package ca.ualberta.moodroid.service;

/**
 * The interface Validation interface.
 */
public interface ValidationInterface {

    /**
     * Ensure that a mood reason is within the spec.
     *
     * @param reason the reason
     * @return boolean
     */
    public boolean isMoodReasonValid(String reason);


    /**
     * Ensure that a username is valid and available
     *
     * @param username the username
     * @return boolean
     */
    public boolean isUsernameAvailable(String username);

    /**
     * Ensure a username is a valid set of characters
     *
     * @param username the username
     * @return boolean
     */
    public boolean isUsernameValid(String username);


}
