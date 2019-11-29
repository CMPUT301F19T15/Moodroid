package ca.ualberta.moodroid.model;

/**
 * Simple model linking a username to firebase auth UID
 */
public class UserModel extends BaseModel {


    private String username;

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
