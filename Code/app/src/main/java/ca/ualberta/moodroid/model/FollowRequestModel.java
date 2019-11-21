package ca.ualberta.moodroid.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FollowRequestModel
 * <p>
 * Extends BaseModel, used to create and interpret friend requests, which can be
 * either accepted or denied by the user
 */
public class FollowRequestModel extends BaseModel {


    /**
     * Name of the user who is requesting to follow
     */
    private String requesterUsername;

    /**
     * User who is being requested to follow
     */
    private String requesteeUsername;

    /**
     * The state of the request.
     */
    private String state;
    private String createdAt;

    /**
     * These are the base states used throughout the app, it changes based on user action,
     */
    public static String ACCEPT_STATE = "accepted";
    /**
     * The constant DENY_STATE.
     */
    public static String DENY_STATE = "declined";
    /**
     * The constant REQUESTED_STATE.
     * <p>
     * Stays in this state if the user does not act upon the request.
     */
    public static String REQUESTED_STATE = "undecided";

    /**
     * Gets the state of the request.
     *
     * @return the state
     */
    public String getState() {
        return state.toLowerCase();
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(String state) {
        this.state = state.toLowerCase();
    }

    /**
     * Gets requester username.
     *
     * @return the requester username
     */
    public String getRequesterUsername() {
        return requesterUsername;
    }

    /**
     * Sets requester username.
     *
     * @param requesterUsername the requester username
     */
    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    /**
     * Gets requestee username.
     *
     * @return the requestee username
     */
    public String getRequesteeUsername() {
        return requesteeUsername;
    }

    /**
     * Sets requestee username.
     *
     * @param requesteeUsername the requestee username
     */
    public void setRequesteeUsername(String requesteeUsername) {
        this.requesteeUsername = requesteeUsername;
    }

    /**
     * Gets created at, which is String information for a time that the request will be created at.
     *
     * @return the created at
     */
    public String getCreatedAt() {
        return this.createdAt;
    }


    /**
     * Sets created at.
     *
     * @param createdAt the created at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Date object date, turns CreatedAt into a date object, then returned.
     *
     * @return the date
     */
    public Date dateObject() {
        return new Date(Long.parseLong(this.getCreatedAt()));
    }

}
