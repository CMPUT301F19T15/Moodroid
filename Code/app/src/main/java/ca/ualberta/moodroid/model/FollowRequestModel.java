package ca.ualberta.moodroid.model;

import java.sql.Timestamp;
import java.util.Date;

public class FollowRequestModel extends BaseModel {


    private String requesterUsername;
    private String requesteeUsername;
    // Possible states: undecided, accepted, declined
    private String state;
    private String createdAt;

    public static String ACCEPT_STATE = "accepted";
    public static String DENY_STATE = "declined";
    public static String REQUESTED_STATE = "undecided";

    public String getState() {
        return state.toLowerCase();
    }

    public void setState(String state) {
        this.state = state.toLowerCase();
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public String getRequesteeUsername() {
        return requesteeUsername;
    }

    public void setRequesteeUsername(String requesteeUsername) {
        this.requesteeUsername = requesteeUsername;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
