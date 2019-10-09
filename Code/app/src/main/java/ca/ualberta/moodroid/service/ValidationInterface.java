package ca.ualberta.moodroid.service;

public interface ValidationInterface {

    /**
     * Ensure that a mood reason is within the spec.
     *
     * @param reason
     * @return
     */
    public boolean isMoodReasonValid(String reason);


    /**
     * Ensure that a username is valid and available
     *
     * @param username
     * @return
     */
    public boolean isUsernameAvailable(String username);

    /**
     * Ensure a username is a valid set of characters
     *
     * @param username
     * @return
     */
    public boolean isUsernameValid(String username);


}
