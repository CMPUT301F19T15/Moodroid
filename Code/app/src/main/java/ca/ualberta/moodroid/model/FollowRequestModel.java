package ca.ualberta.moodroid.model;

public class FollowRequestModel extends BaseModel {


    private String requesterUsername;
    private String requesteeUsername;
    // Possible states: undecided, accepted, denied
    private String state;

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
}
